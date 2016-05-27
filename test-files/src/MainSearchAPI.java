package com.bbva.hoggtoolsp.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bbva.hoggtoolsp.dao.DefinicionProcesoDao;
import com.bbva.hoggtoolsp.dao.DescripcionDao;
import com.bbva.hoggtoolsp.dao.ProcesoDao;
import com.bbva.hoggtoolsp.model.DefinicionProcesoEntity;
import com.bbva.hoggtoolsp.model.DescripcionEntity;
import com.bbva.hoggtoolsp.model.ProcesoEntity;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

public class MainSearchAPI {
	
	//TODO: clase de filtros contemplados para construir la QUERY
	//TODO: obtener Procesos de AU
	//	static final String COOKIE_AU = "ACSID=AJKiYcFIXFvLv6FSxGHZIWnnJzp_heYgrgOYr7VaLVdze6n9tdB4fz2hk3-oQ5uwNZ5AMKHksIlu8tgd_WXjBUNU2rIhJBKj5g212De7OyqMnhy71ebeJ69allXVmghGFbQ1zjLk7Qv1ivvLVmcOCmOnvre99_Z__FZfGeynUyqBTvSguHUPqsmRfqXZ--sFHx430gZ6TP_dmI9hBAheqGCV3W75sEM8_lBT9cHltI0GIu-Uqst2ib0DmAQTMIt4jTDtjsB5lBYKRtEgI3tpiQUC5H5vAtujI4YniG6OMl4iJx4PVeMQTlH0YHfUkymYDy19pmtz1TGgWXeGpkgTlbLa2YWgaYJHLUUoVCW3dJZFOomJBhD4U7ZH0t7KPLJaNj-UMAGfNIaFqrYmSn569H1HP2Fwd2OLS8izkkUea2MAYGSwD9JnTc5h2B51UlVHy7JaVHmYLTo1dMCJqwSmBG_6LfIahxiTxle85-UEugvVX-Wui5lO4pbuPfT-iSKi2-ykZ0wMLNCcMtMMJJZZN0TC9ZdwCPEizCAtoS4mFZoBq-xr7tkF8Qw0-Xbb0Rg8wp0VjPwXYBOzfT9O33AOHuH_W5p5nojIVku6ji_04AlwkJDjMAW7s3iDwbILGOrAE_g1wvMxP65YmhGN5wD8ViFcRjaPzyLItYwenZpWskZ62tGgWZVa0Aiccg6P1YVFer0p103lAPoLouFa0pz04PLpFsKf8AoPjyBti2J-okc2YMVd9T5ZVi1h4bX1m-7IP0hqs6kH7yAtrVzr5NZ4XwO04H0-7thCokPbg2Q8O8kbyeJagZAP9wpOgib3cPpMLEGVNk5jV0cIQ0pOaC5Y7cVsGjtiLg0MWM1l6k-tJa6qhONgP51_q3uoCcdgDPhJgQy4DpGQSMxUOe6cruTfRvnD88aDKTrQnm93BpADpiWmvuRalunmFSbwg702pozJNMZfImKK5lRIFHHTVpKyJTPcz6fihWoX1DaCt_GhbTx1mvWfdmZ1sfCSnxJmEjFKiGepTcpAvKMRMlEBZs8FZ1rCFX8o07VMRGfV39DQnJFzPN3bD6HyurcRvN-cHB0rCwt0meqt4yifIZwNaaJnPSx4oYfIRVhgevl2VdnKjK1JUSNF8NeJnIMvTazAI2IIWYPCrem7WCZt";

	
	private static final String INDEX_PROCESOS = "ProcesoEntityIndex";

	private TablasParametrizablesDataOnDemand dod = new TablasParametrizablesDataOnDemand();
	private DefinicionProcesoDao definicionProcesoDao = new DefinicionProcesoDao();
	private DescripcionDao descripcionDao = new DescripcionDao();
	private ProcesoDao procesoDao = new ProcesoDao();

	private List<DefinicionProcesoEntity> getAllDps() {
		return this.definicionProcesoDao.list(true);
	}
	
	public DescripcionEntity getDescripcion(Long id) {
		return this.descripcionDao.get(id);
	}
	
	public ProcesoEntity getProceso(String id) {
		return this.procesoDao.get(id);
	}

	public void init() {
		dod.setUp();
	}

	public static void main(String[] args) {
		
		MainSearchAPI mainSearchAPI = new MainSearchAPI();
		
		mainSearchAPI.init();
		
		testInit(mainSearchAPI);
		
		Index index = indexLookup(INDEX_PROCESOS);
		List<ProcesoEntity> procesos = new ArrayList<>(); //TODO: buscar
		populatingIndex(index, procesos);
		
		String searchString = "fecha_creacion = 2014-12-01";
		Query query = Query.newBuilder().build(searchString);
		
		Results<ScoredDocument> results = index.search(query);
		
		System.out.printf("Obtenidos %d DOCUMENTOS de un total de %d\n", results.getNumberReturned(), results.getNumberFound());
		
		for (ScoredDocument scoredDocument : results) {
			String doc_id = scoredDocument.getId();
			String id = scoredDocument.getOnlyField(ProcesoEntityAsDocumentFieldNames.id.name()).getAtom();
			Long estadoProceso = scoredDocument.getOnlyField(ProcesoEntityAsDocumentFieldNames.estadoProceso.name()).getNumber().longValue();
			String id_definicion_proceso = scoredDocument.getOnlyField(ProcesoEntityAsDocumentFieldNames.id_definicion_proceso.name()).getText();
			Date fecha_creacion  = scoredDocument.getOnlyField(ProcesoEntityAsDocumentFieldNames.fecha_creacion.name()).getDate();
			
			System.out.printf("Encontrado DOCUMENTO con id %s correspondiente al proceso: id=%s, estadoProceso=%d, id_definicion_proceso=%s, fecha_creacion=%tF \n", doc_id, id, estadoProceso, id_definicion_proceso, fecha_creacion);
			
			ProcesoEntity procesoEntity = mainSearchAPI.getProceso(id);
			
			if (procesoEntity == null) {
				System.err.printf("Error, no encontrado el proceso con id %s que sí está en el índice con id %s\n", id, doc_id);
			}
			else {
				System.out.printf("Encontrado el PROCESO con id %s correspondiente al documento con id %s\n", id, doc_id);
			}
			
		}
		
	}
	
	private static Index indexLookup(String indexName) {
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
	    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	    return index;
	}
	
	private static void populatingIndex(Index index, List<ProcesoEntity> procesos) {
		for (ProcesoEntity procesoEntity : procesos) {
			indexADocument(index, procesoToDocument(procesoEntity));
		}
	}
	
	public static void indexADocument(Index index, Document document) {
	    try {
	        index.put(document);
	    } catch (PutException e) {
	        if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
	        	index.put(document);
	        }
	    }
	}	

	public static Document procesoToDocument(ProcesoEntity procesoEntity) {
		Document document = Document.newBuilder().setId(procesoEntity.getId())
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.id.name()).setAtom(procesoEntity.getId()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.estadoProceso.name()).setNumber(procesoEntity.getEstadoProceso()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.id_definicion_proceso.name()).setText(procesoEntity.getId_definicion_proceso()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.userFirstname.name()).setText(procesoEntity.getUserFirstname()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.userLastname.name()).setText(procesoEntity.getUserLastname()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.tipoDePeticion.name()).setText(procesoEntity.getTipoDePeticion()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.id_padre.name()).setText(procesoEntity.getId_padre()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.uo_peticionaria.name()).setText(procesoEntity.getUo_peticionaria()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.sla_restante.name()).setNumber(procesoEntity.getSla_restante()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.fecha_creacion.name()).setDate(new Date(procesoEntity.getFecha_creacion())))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.usuario_responsable.name()).setText(procesoEntity.getUsuario_responsable()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.ambito.name()).setText(procesoEntity.getAmbito()))
				.addField(Field.newBuilder().setName(ProcesoEntityAsDocumentFieldNames.grupo_actual.name()).setText(procesoEntity.getGrupo_actual()))
				.build();
		return document;
	}
	
	private static void testInit(MainSearchAPI mainSearchAPI) {
		System.out.println("****** TEST INIT *****");
		List<DefinicionProcesoEntity> dps = mainSearchAPI.getAllDps();
		for (DefinicionProcesoEntity dp : dps) {
			String descripcion = null;
			DescripcionEntity descripcionEntity = mainSearchAPI.getDescripcion(dp.getDescripcion());
			if (descripcionEntity != null) {
				descripcion = descripcionEntity.getDescripcionES();
			}
			System.out.printf("DP: %s - %s\n", dp.getId(), descripcion);
		}
		System.out.println("****** END: TEST INIT *****");
	}
	
	public enum ProcesoEntityAsDocumentFieldNames {
		
		id, estadoProceso, id_definicion_proceso, userFirstname, userLastname, tipoDePeticion, id_padre, uo_peticionaria,
		sla_restante, fecha_creacion, usuario_responsable, ambito, grupo_actual;
		
	}

}

package mx.com.lobo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ConvierteObjetos {

	public static ResponseApi convierte(Boolean success, String message, Object data) {
		data = procesaData(data);
		ResponseApi reponse = new ResponseApi(success, message, data);
		return reponse;
	}

	public static ResponseApi convierte(Object data) {
		data = procesaData(data);
		ResponseApi reponse = new ResponseApi(true, "Proceso realizado corretamente", data);
		return reponse;
	}

	private static Object procesaData(Object data) {
		if (data == null) {
			data = new ArrayList<>();
		} else {
			System.out.println(data.getClass().getSimpleName());
			
			switch (data.getClass().getSimpleName()) {
			case "ArrayList":
				List<?> lista = (List<?>) data;
				if (lista != null && !lista.isEmpty()) {
					if (lista.get(0).getClass().getSimpleName().equals("HqlTupleImpl")) {
						data = _toJson((List<Tuple>) data);
					}
				}
				break;
			}
		}
		return data;
	}

	private static List<ObjectNode> _toJson(List<Tuple> data) {
		List<ObjectNode> json = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (Tuple t : data) {
			List<TupleElement<?>> cols = t.getElements();
			ObjectNode one = mapper.createObjectNode();
			for (TupleElement<?> col : cols) {
				one.put(col.getAlias(), t.get(col.getAlias()).toString());
			}
			json.add(one);
		}
		return json;
	}
}
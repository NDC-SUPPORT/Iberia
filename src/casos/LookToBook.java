package casos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipalInformes;

import beans.BeanFormularioInformes;
import beans.BeanSheetExcelLookToBook;

public class LookToBook {
	
	public List<BeanSheetExcelLookToBook> obtenerDatos(BeanFormularioInformes bF) 
	{
		List<BeanSheetExcelLookToBook> myList = new ArrayList<BeanSheetExcelLookToBook>();
		
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptLook.sh" + " " + "2020-03-04" + " " + "2020-03-04");

			if (responseKibana != null)
			{				
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject aggregations = jsonResponse.getJSONObject("aggregations");
				JSONObject usuarios = aggregations.getJSONObject("usuarios");
				JSONArray bucketsArray = usuarios.getJSONArray("buckets");

				for (int i = 0; i < bucketsArray.length(); i++) 
				{
					JSONObject objArray = bucketsArray.getJSONObject(i);
					String user = objArray.getString("key");
					Long look = objArray.getLong("doc_count");
					
					BeanSheetExcelLookToBook beanL2B = new BeanSheetExcelLookToBook();
					beanL2B.setUser(user);
					beanL2B.setLook(look);
					
					myList.add(beanL2B);
				}
			}
			
			return myList;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalInformes.showError(e.getMessage());
			return null;
		}
	}
}

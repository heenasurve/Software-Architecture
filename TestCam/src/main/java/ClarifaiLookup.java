import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import java.io.File;
import java.util.ArrayList;

import clarifai2.api.ClarifaiResponse;
public class ClarifaiLookup {

    public static void lookupImage(byte[] img){

       /* try {
            Thread.sleep(5000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }*/
        ClarifaiClient client = new ClarifaiBuilder("d979d5c4cf62431a97a26b9c9dc8c920")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync();
        ClarifaiResponse response = client.getDefaultModels().generalModel().predict()
                .withInputs(ClarifaiInput.forImage(img))
                .executeSync();

        //System.out.print(response.rawBody()

        Gson gson = new Gson();
        JsonElement element = gson.fromJson (response.rawBody(), JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();
        JsonArray outputs = jsonObj.get("outputs").getAsJsonArray();
        JsonElement data = outputs.get(0).getAsJsonObject().get("data").getAsJsonObject().get("concepts").getAsJsonArray();
        ArrayList<String> concepts = new ArrayList<>();
        for(JsonElement concept: data.getAsJsonArray()){
            //objects
            if(concept.getAsJsonObject().get("name").toString().replace("\"","").equals("abstract")){
                System.out.println("I don't see anything");
                break;
            }else {
                if (concepts.size() < 3) {
                    if(!concept.getAsJsonObject().get("name").toString().replace("\"", "").equals("portrait"))
                        concepts.add(concept.getAsJsonObject().get("name").toString().replace("\"", ""));
                } else {
                    break;
                }
            }
        }
        if(!concepts.isEmpty()){
            System.out.println("I am seeing " + concepts.get(0) + " , " + concepts.get(1) + " , " + concepts.get(2) );
        }
    }
}
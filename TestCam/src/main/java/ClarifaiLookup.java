import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import java.io.File;
import clarifai2.api.ClarifaiResponse;

public class ClarifaiLookup {

    public static void lookupImage(byte[] img){

        ClarifaiClient client = new ClarifaiBuilder("d979d5c4cf62431a97a26b9c9dc8c920")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync();
        ClarifaiResponse response = client.getDefaultModels().generalModel().predict()
                .withInputs(ClarifaiInput.forImage(img))
                .executeSync();
        Gson gson = new Gson();
        JsonElement element = gson.fromJson (response.rawBody(), JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();
        JsonArray outputs = jsonObj.get("outputs").getAsJsonArray();
        JsonElement sdfsd = outputs.get(0).getAsJsonObject().get("data");
        System.out.println(sdfsd);
        //System.out.println(response.rawBody()
        ;
    }
}
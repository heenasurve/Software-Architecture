import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
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
        System.out.print(response.rawBody()
        );
    }
}
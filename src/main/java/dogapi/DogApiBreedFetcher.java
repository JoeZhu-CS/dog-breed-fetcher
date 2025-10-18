package dogapi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        try {
            String url = "https://dog.ceo/api/breed/" + breed + "/list";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new BreedNotFoundException(breed);
                }
                
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                
                if (!"success".equals(jsonResponse.getString("status"))) {
                    throw new BreedNotFoundException(breed);
                }
                
                JSONArray subBreedsArray = jsonResponse.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();
                
                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));
                }
                
                return subBreeds;
            }
        } catch (Exception e) {
            throw new BreedNotFoundException(breed);
        }
    }
}
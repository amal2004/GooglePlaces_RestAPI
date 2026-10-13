package googleplaces.client;



import static io.restassured.RestAssured.given;

import googleplaces.config.RequestSpecFactory;
import googleplaces.data.TestDataBuilder;
import io.restassured.response.Response;
import googleplaces.resources.APIResources;
import googleplaces.pojo.AddPlace;

/** HTTP operations only; assertions belong in the test/step layer. */
public class PlaceApiClient {
    private final TestDataBuilder dataBuilder = new TestDataBuilder();

    public Response addPlace(String name, String language, String address) {
        AddPlace payload = dataBuilder.addPlace(name, language, address);
        return given()
                .spec(RequestSpecFactory.create())
                .body(payload)
                .when()
                .post(APIResources.ADD_PLACE.getPath());
    }

    public Response getPlace(String placeId) {
        return given()
                .spec(RequestSpecFactory.create())
                .queryParam("place_id", placeId)
                .when()
                .get(APIResources.GET_PLACE.getPath());
    }

    public Response deletePlace(String placeId) {
        return given()
                .spec(RequestSpecFactory.create())
                .body(dataBuilder.deletePlace(placeId))
                .when()
                .post(APIResources.DELETE_PLACE.getPath());
    }
}

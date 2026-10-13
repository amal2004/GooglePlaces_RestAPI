package googleplaces.steps;

import googleplaces.client.PlaceApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

public class PlaceApiSteps {
    private final PlaceApiClient api = new PlaceApiClient();
    private Response response;
    private String placeId;

    @When("I add a place named {string} in {string} at {string}")
    public void addPlace(String name, String language, String address) {
        response = api.addPlace(name, language, address);
        if (response.statusCode() == 200 && "OK".equals(response.jsonPath().getString("status"))) {
            placeId = response.jsonPath().getString("place_id");
        }
    }

    @Given("a place named {string} in {string} at {string} has been created")
    public void createPlaceForScenario(String name, String language, String address) {
        response = api.addPlace(name, language, address);
        Assert.assertEquals(response.statusCode(), 200, "Place setup request should return HTTP 200.");
        Assert.assertEquals(response.jsonPath().getString("status"), "OK", "Place setup should succeed.");
        placeId = response.jsonPath().getString("place_id");
        Assert.assertNotNull(placeId, "The add-place response should contain place_id.");
    }

    @When("I retrieve the created place")
    public void retrieveCreatedPlace() {
        Assert.assertNotNull(placeId, "A place must be created before retrieving it.");
        response = api.getPlace(placeId);
    }

    @When("I retrieve a place using ID {string}")
    public void retrievePlaceById(String id) {
        response = api.getPlace(id);
    }

    @When("I delete the created place")
    public void deleteCreatedPlace() {
        Assert.assertNotNull(placeId, "A place must be created before deleting it.");
        response = api.deletePlace(placeId);
    }

    @When("I delete a place using ID {string}")
    public void deletePlaceById(String id) {
        response = api.deletePlace(id);
    }

    @Then("the HTTP status code is {int}")
    public void verifyHttpStatus(int expectedStatus) {
        Assert.assertEquals(response.statusCode(), expectedStatus, "Unexpected HTTP status. Response: " + response.asString());
    }

    @Then("the response field {string} equals {string}")
    public void verifyResponseField(String field, String expectedValue) {
        Assert.assertEquals(response.jsonPath().getString(field), expectedValue,
                "Unexpected value for response field: " + field);
    }

    @Then("the response contains a place ID")
    public void verifyPlaceIdExists() {
        placeId = response.jsonPath().getString("place_id");
        Assert.assertNotNull(placeId, "place_id should be present in the response.");
        Assert.assertFalse(placeId.trim().isEmpty(), "place_id should not be blank.");
    }

    @Then("the retrieved place name is {string}")
    public void verifyRetrievedName(String expectedName) {
        Assert.assertEquals(response.jsonPath().getString("name"), expectedName);
    }

    @Then("the retrieved place address is {string}")
    public void verifyRetrievedAddress(String expectedAddress) {
        Assert.assertEquals(response.jsonPath().getString("address"), expectedAddress);
    }

    @Then("the response content type is JSON")
    public void verifyJsonContentType() {
        Assert.assertTrue(response.getContentType().toLowerCase().contains("application/json"),
                "Expected JSON content type but got: " + response.getContentType());
    }
}

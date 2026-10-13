package googleplaces.data;

import googleplaces.pojo.AddPlace;
import googleplaces.pojo.Location;

import java.util.Arrays;

/** Builds request payloads in one place so scenarios remain short and readable. */
public class TestDataBuilder {
    public AddPlace addPlace(String name, String language, String address) {
        AddPlace place = new AddPlace();
        place.setAccuracy(50);
        place.setName(name);
        place.setLanguage(language);
        place.setAddress(address);
        place.setPhone_number("(+1) 555-0100");
        place.setWebsite("https://example.com");
        place.setTypes(Arrays.asList("shoe park", "shop"));
        place.setLocation(new Location(-38.383494, 33.427362));
        return place;
    }

    public DeletePlaceRequest deletePlace(String placeId) {
        return new DeletePlaceRequest(placeId);
    }

    public static class DeletePlaceRequest {
        private final String place_id;

        public DeletePlaceRequest(String placeId) {
            this.place_id = placeId;
        }

        public String getPlace_id() {
            return place_id;
        }
    }
}

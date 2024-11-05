package com.example.traveladviser.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeocodingResponse {

    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public class Result {

        @SerializedName("geometry")
        private Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }

        public class Geometry {

            @SerializedName("location")
            private Coordinates coordinates;  // Using Coordinates class here

            @SerializedName("bounds")
            private Bounds bounds;  // Adding Bounds class here

            public Coordinates getCoordinates() {
                return coordinates;
            }

            public Bounds getBounds() {
                return bounds;
            }

            public class Coordinates {
                @SerializedName("lat")
                private double lat;

                @SerializedName("lng")
                private double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public class Bounds {
                @SerializedName("northeast")
                private Coordinates northeast;

                @SerializedName("southwest")
                private Coordinates southwest;

                public Coordinates getNortheast() {
                    return northeast;
                }

                public Coordinates getSouthwest() {
                    return southwest;
                }
            }
        }
    }
}
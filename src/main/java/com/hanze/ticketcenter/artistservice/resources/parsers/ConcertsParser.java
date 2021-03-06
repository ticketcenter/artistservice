package com.hanze.ticketcenter.artistservice.resources.parsers;

import com.hanze.ticketcenter.artistservice.dto.ConcertsDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The concerts parser.
 *
 * @author      Nils Berlijn
 * @version     1.0
 * @since       1.0
 */
public class ConcertsParser {
    /**
     * Parse concerts into a concerts DTO.
     *
     * @param concerts          The concerts to parse.
     * @return                  A concerts DTO.
     * @see                     com.hanze.ticketcenter.artistservice.dto.ConcertsDTO
     */
    public ConcertsDTO parseConcerts(String concerts) {
        ConcertsDTO concertsDTO = new ConcertsDTO();

        try {
            JSONObject oldConcerts = (JSONObject) new JSONParser().parse(concerts);

            if (!oldConcerts.get("total_items").equals("0")) {
                concertsDTO.setTotalItems(Integer.parseInt((String) oldConcerts.get("total_items")));
                concertsDTO.setPageSize(Integer.parseInt((String) oldConcerts.get("page_size")));
                concertsDTO.setPageNumber(Integer.parseInt((String) oldConcerts.get("page_number")));
                concertsDTO.setPageCount(Integer.parseInt((String) oldConcerts.get("page_count")));
                concertsDTO.setConcerts(parseEvents((JSONObject) oldConcerts.get("events")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return concertsDTO;
    }

    /**
     * Parse a concert into a concerts DTO.
     *
     * @param concert           The concert to parse.
     * @return                  A concerts DTO.
     * @see                     com.hanze.ticketcenter.artistservice.dto.ConcertsDTO
     */
    public ConcertsDTO parseConcert(String concert) {
        ConcertsDTO concertsDTO = new ConcertsDTO();

        try {
            JSONObject oldConcert = (JSONObject) new JSONParser().parse(concert);

            if (oldConcert.get("status") == null) {
                concertsDTO.setConcerts(parseConcertEvent(oldConcert));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return concertsDTO;
    }

    /**
     * Parse events into a map.
     *
     * @param events            The events to parse.
     * @return                  Parsed events.
     */
    private Map<String, List> parseEvents(JSONObject events) {
        Map<String, List> newConcerts = new LinkedHashMap<>();

        if (events != null) {
            Object event = events.get("event");
            List<Map> newConcertsList = new LinkedList<>();

            if (event instanceof JSONObject) {
                JSONObject eventJsonObject = (JSONObject) event;
                newConcertsList.add(parseConcertsEvent(eventJsonObject));
            } else if (event instanceof JSONArray) {
                JSONArray eventJsonArray = (JSONArray) event;

                for (Object eventJsonObject : eventJsonArray) {
                    newConcertsList.add(parseConcertsEvent((JSONObject) eventJsonObject));
                }
            }

            newConcerts.put("concert", newConcertsList);
        } else {
            newConcerts = null;
        }

        return newConcerts;
    }

    /**
     * Parse an event into a map.
     *
     * @param event             The event to parse.
     * @return                  A parsed event.
     */
    private Map<String, Object> parseConcertsEvent(JSONObject event) {
        Map<String, Object> newConcert = new LinkedHashMap<>();

        if (event != null) {
            newConcert.put("id", event.get("id"));
            newConcert.put("title", event.get("title"));
            newConcert.put("artists", parsePerformers((JSONObject) event.get("performers")));
            newConcert.put("image", parseImage((JSONObject) event.get("image")));
            newConcert.put("country", event.get("country_name"));
            newConcert.put("region", event.get("region_name"));
            newConcert.put("city", event.get("city_name"));
            newConcert.put("start_time", event.get("start_time"));
            newConcert.put("tickets_available", parseTicketsAvailable((String) event.get("longitude")));
            newConcert.put("ticket_price", parseTicketPrice((String) event.get("latitude")));
        } else {
            newConcert = null;
        }

        return newConcert;
    }

    /**
     * Parse an event into a map.
     *
     * @param event             The event to parse.
     * @return                  A parsed event.
     */
    private Map<String, Object> parseConcertEvent(JSONObject event) {
        Map<String, Object> newConcert = new LinkedHashMap<>();

        if (event != null) {
            newConcert.put("id", event.get("id"));
            newConcert.put("title", event.get("title"));
            newConcert.put("artists", parsePerformers((JSONObject) event.get("performers")));
            newConcert.put("image", parseImages((JSONObject) event.get("images")));
            newConcert.put("country", event.get("country"));
            newConcert.put("region", event.get("region"));
            newConcert.put("city", event.get("city"));
            newConcert.put("start_time", event.get("start_time"));
            newConcert.put("tickets_available", parseTicketsAvailable((String) event.get("longitude")));
            newConcert.put("ticket_price", parseTicketPrice((String) event.get("latitude")));
        } else {
            newConcert = null;
        }

        return newConcert;
    }

    /**
     * Parse performers into a map.
     *
     * @param performers        The performers to parse.
     * @return                  Parsed performers.
     */
    private Map<String, List> parsePerformers(JSONObject performers) {
        Map<String, List> newArtists = new LinkedHashMap<>();

        if (performers != null) {
            Object performer = performers.get("performer");
            List<Map> netPerformersList = new LinkedList<>();

            if (performer instanceof JSONObject) {
                JSONObject eventJsonObject = (JSONObject) performer;
                netPerformersList.add(parsePerformer(eventJsonObject));
            } else if (performer instanceof JSONArray) {
                JSONArray eventJsonArray = (JSONArray) performer;

                for (Object eventJsonObject : eventJsonArray) {
                    netPerformersList.add(parsePerformer((JSONObject) eventJsonObject));
                }
            }

            newArtists.put("artist", netPerformersList);
        } else {
            newArtists = null;
        }

        return newArtists;
    }

    /**
     * Parse a performer into a map.
     *
     * @param performer         The performer to parse.
     * @return                  A parsed performer.
     */
    private Map parsePerformer(JSONObject performer) {
        Map<String, Object> newArtist = new LinkedHashMap<>();

        if (performer != null) {
            newArtist.put("name", performer.get("name"));
        } else {
            newArtist = null;
        }

        return newArtist;
    }

    /**
     * Parse an image into a map.
     *
     * @param image             The image to parse.
     * @return                  A parsed image.
     */
    private Map<String, Object> parseImage(JSONObject image) {
        Map<String, Object> newImage = new LinkedHashMap<>();

        if (image != null) {
            newImage.put("url", image.get("url"));
        } else {
            newImage = null;
        }

        return newImage;
    }

    /**
     * Parse images into a map.
     *
     * @param images            The images to parse.
     * @return                  Parsed images.
     */
    private Map<String, Object> parseImages(JSONObject images) {
        Map<String, Object> newImage = new LinkedHashMap<>();

        if (images != null) {
            Object image = images.get("image");

            if (image instanceof JSONObject) {
                JSONObject imageJsonObject = (JSONObject) image;
                newImage.putAll(parseImage(imageJsonObject));
            } else if (image instanceof JSONArray) {
                JSONArray imageJsonArray = (JSONArray) image;
                newImage.putAll(parseImage((JSONObject) imageJsonArray.get(0)));
            }
        } else {
            newImage = null;
        }

        return newImage;
    }

    /**
     * Parse tickets available into a integer.
     *
     * @param ticketsAvailable  The tickets available to parse.
     * @return                  The tickets available.
     */
    private Integer parseTicketsAvailable(String ticketsAvailable) {
        String[] split = ticketsAvailable.replace("-", "").split("\\.");
        Integer price = 0;

        try {
            if (split.length == 2) {
                price = Integer.parseInt(split[1]) / 5000;
            } else {
                price = Integer.parseInt(split[0]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return price;
    }

    /**
     * Parse the ticket price into a double.
     *
     * @param ticketPrice       The ticket price to parse.
     * @return                  The ticket price.
     */
    private Double parseTicketPrice(String ticketPrice) {
        String[] split = ticketPrice.replace("-", "").split("\\.");
        Double price = 0.0;

        try {
            if (split.length == 2) {
                price = Double.parseDouble(split[1]) / 20000;

                if (price <= 10) {
                    price += 10;
                } else if (price > 300) {
                    price = 300.0;
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                price = Double.valueOf(decimalFormat.format(price));
            } else {
                price = Double.parseDouble(split[0]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return price;
    }
}

package com.hanze.ticketcenter.artistservice.dao.interfaces;

public interface ArtistsDAOInterface {
    public String getArtists(String characters, String pageSize, String pageNumber);
    public String getArtist(String name);
}
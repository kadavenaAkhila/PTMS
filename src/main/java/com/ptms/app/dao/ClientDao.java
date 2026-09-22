package com.ptms.app.dao;

import com.ptms.app.model.Client;

import java.util.List;

public interface ClientDao {

    boolean addClient(Client client);

    boolean updateClient(Client client);

    boolean deleteClient(int clientId);

    Client getClientById(int clientId);

    List<Client> getAllClients();

    List<Client> searchClients(String keyword);
}
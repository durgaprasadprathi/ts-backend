package com.appmodz.executionmodule.service;

import com.appmodz.executionmodule.dao.OrganizationDAO;
import com.appmodz.executionmodule.dto.SearchResultDTO;
import com.appmodz.executionmodule.dto.SearchRequestDTO;
import com.appmodz.executionmodule.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    OrganizationDAO organizationDAO;

    public Organization getOrganizationById(long organizationId) {
        return organizationDAO.get(organizationId);
    }

    public Organization createOrganization(String name) {
        Organization organization = new Organization();
        organization.setOrganizationName(name);
        organizationDAO.save(organization);
        return organization;
    }

    public Organization updateOrganization(long organizationId, String name) {
        Organization organization = organizationDAO.get(organizationId);
        organization.setOrganizationName(name);
        organizationDAO.save(organization);
        return organization;
    }

    public void deleteOrganization(Organization organization) {
        organizationDAO.delete(organization);
    }

    public List listOrganizations() {
        return organizationDAO.getAll();
    }

    public SearchResultDTO searchOrganizations(SearchRequestDTO searchRequestDTO) {
        return organizationDAO.search(searchRequestDTO);
    }
}

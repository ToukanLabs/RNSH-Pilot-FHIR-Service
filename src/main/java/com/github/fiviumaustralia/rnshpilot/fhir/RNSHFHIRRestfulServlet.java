package com.github.fiviumaustralia.rnshpilot.fhir;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

import java.util.ArrayList;
import java.util.List;

public class RNSHFHIRRestfulServlet extends RestfulServer {

  /**
   * Constructor
   */
  public RNSHFHIRRestfulServlet() {
    List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
    resourceProviders.add(new RestfulPatientResourceProvider());
    setResourceProviders(resourceProviders);
  }

}
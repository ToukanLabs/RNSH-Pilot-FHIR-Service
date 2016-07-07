package com.github.fiviumaustralia.rnshpilot.fhir;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fiviumaustralia.rnshpilot.FiviumPatient;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;

/**
  * All resource providers must implement IResourceProvider
  */
public class RestfulPatientResourceProvider implements IResourceProvider {

  private RPCClient rpcClient;

  public RestfulPatientResourceProvider() {
    super();
    try {
      this.rpcClient = new RPCClient("java-hapifhir-service", "java-hapifhir-service", "localhost", 5672);
    } catch (Exception ex) {
      System.err.println("Unable to connect to RabbitMQ.");
      System.err.println(ex);
      System.exit(1);
    }
  }

  /**
    * The getResourceType method comes from IResourceProvider, and must
    * be overridden to indicate what type of resource this provider
    * supplies.
    */
  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  /**
    * The "@Read" annotation indicates that this method supports the
    * read operation. Read operations should return a single resource
    * instance.
    *
    * @param theId
    *    The read operation takes one parameter, which must be of type
    *    IdDt and must be annotated with the "@Read.IdParam" annotation.
    * @return
    *    Returns a resource matching this identifier, or null if none exists.
    */
    @Read()
    public Patient getResourceById(@IdParam IdDt theId) throws Exception {
      String patientId = theId.getIdPart();

      byte[] res = rpcClient.call("GetPatient", "{\"method\": \"GetPatient\", \"params\": {\"id\": " + patientId + "}}");

      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      FiviumPatient fiviumPatient = mapper.readValue(res, FiviumPatient.class);

      res = rpcClient.call("GetEhrId", "{\"method\": \"GetEhrId\", \"params\": {\"MRN\": \"" + fiviumPatient.getMrn() + "\"}}");
      RPCResultGetEhrId getEhrResult = mapper.readValue(res, RPCResultGetEhrId.class);

      Patient patient = new Patient();
      patient.addIdentifier();
      patient.getIdentifier().get(0).setSystem(new UriDt("id"));
      patient.getIdentifier().get(0).setValue(fiviumPatient.getId());
      patient.addIdentifier();
      patient.getIdentifier().get(1).setSystem(new UriDt("mrn"));
      patient.getIdentifier().get(1).setValue(fiviumPatient.getMrn());
      patient.addIdentifier();
      patient.getIdentifier().get(2).setSystem(new UriDt("ehrId"));
      patient.getIdentifier().get(2).setValue(getEhrResult.getEhrId());
      Calendar dobCal = DatatypeConverter.parseDate(fiviumPatient.getDob());
      patient.setBirthDate(dobCal.getTime(), TemporalPrecisionEnum.DAY);
      patient.addName();
      patient.getName().get(0).addGiven(fiviumPatient.getFirstname());
      patient.getName().get(0).addFamily(fiviumPatient.getSurname());
      patient.addContact();
      patient.getContact().get(0).addTelecom();
      patient.getContact().get(0).getTelecom().get(0).setSystem(ContactSystemEnum.PHONE);
      patient.getContact().get(0).getTelecom().get(0).setValue(fiviumPatient.getPhone());
      patient.addContact();
      patient.getContact().get(1).addTelecom();
      patient.getContact().get(1).getTelecom().get(0).setSystem(ContactSystemEnum.EMAIL);
      patient.getContact().get(1).getTelecom().get(0).setValue(fiviumPatient.getEmail());

      if (fiviumPatient.getGender().equals("FEMALE")) {
        patient.setGender(AdministrativeGenderCodesEnum.F);
      } else if (fiviumPatient.getGender().equals("MALE")) {
        patient.setGender(AdministrativeGenderCodesEnum.M);
      } else {
        patient.setGender(AdministrativeGenderCodesEnum.UNK);
      }

//      private String tumorType;
//      private String surgical;

      return patient;
    }
}

class RPCResultGetEhrId {
  private String ehrId;

  public void setEhrId(String ehrId) {
    this.ehrId = ehrId;
  }
  public String getEhrId() {
    return this.ehrId;
  }
}
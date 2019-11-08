package consulta;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class StepDefinition {
    private HttpURLConnection conn;
    private int statusCodeResponse;

    @Given("^Luis deseja consultar o endereço de um correntista$")
    public void set_endpoint() throws IOException {
        URL url = new URL("http://localhost:80/api/v1/holders/address");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
    }

    @When("^Luis define os parametros da consulta$")
    public void set_header(DataTable dt) {

        List<Map<String, String>> params = dt.asMaps(String.class, String.class);
        for (int i = 0; i < params.size(); i++) {
            conn.setRequestProperty("cpf", params.get(i).get("cpf"));
            conn.setRequestProperty("accountType", params.get(i).get("accountType"));
        }
    }

    @And("^realiza sua consulta$")
    public void execute() throws IOException {

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
        } else {
            statusCodeResponse = conn.getResponseCode();
        }
    }

    @Then("^o retorno da requisição deverá ser (\\d+)$")
    public void validate_status_code_response(int status) {
        Assert.assertEquals(status, 200);
    }

    @And("^o retorno da requisição deve conter um json com os seguintes dados$")
    public void validate_responseBody(DataTable dt) throws IOException {
        JSONObject response = new JSONObject();

        List<Map<String, String>> params = dt.asMaps(String.class, String.class);

        //get the response content and parse to json
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        //System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            //System.out.println(output);
            response = new JSONObject(output);
        }
        //System.out.println(response);
        Assert.assertEquals(response.get("zipCode"), params.get(0).get("zipCode"));
        Assert.assertEquals(response.get("street"), params.get(0).get("street"));
        Assert.assertEquals(response.get("number"), params.get(0).get("number"));
        Assert.assertEquals(response.get("complement"), params.get(0).get("complement"));
        conn.disconnect();
    }
}

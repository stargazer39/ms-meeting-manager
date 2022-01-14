package com.msclub.meetingmanager.service;

import com.google.gson.Gson;
import com.msclub.meetingmanager.model.MicrosoftCredentials;
import com.msclub.meetingmanager.model.MicrosoftTeamsMeetingDetails;
import com.msclub.meetingmanager.model.microsoftteamsmeet.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;

@Service
public class MicrosoftTeamsService {

    static RestTemplate restTemplate = new RestTemplate();

    public String scheduleMeeting() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id","");
        map.add("client_secret","");
        map.add("grant_type","");
        map.add("scope","");
        map.add("refresh_token","");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<MicrosoftCredentials> response =
                restTemplate.exchange("",
                        HttpMethod.POST,
                        entity,
                        MicrosoftCredentials.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return scheduleMeetingTest(response.getBody().getAccess_token());
        } else {
            return null;
        }

    }

    public String scheduleMeetingTest(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        Body body = new Body("HTML","Thank you for applying with MS Club SLIIT. We would like to invite you for the next step of your application. Does this time work for you?");
        Start start = new Start("2022-01-14T23:30:00","India Standard Time");
        End end = new End("2022-01-14T23:50:00","India Standard Time");
        Location location = new Location("MS Club Conference Room");

        EmailAddress emailAddress = new EmailAddress("it19139036@my.sliit.lk");

        ArrayList<Attendee> attendees = new ArrayList<>();
        Attendee attendee = new Attendee(emailAddress,"required");
        attendees.add(attendee);

        // create request body
        MicrosoftTeamsMeetingDetails microsoftTeamsMeetingDetails = new MicrosoftTeamsMeetingDetails();
        microsoftTeamsMeetingDetails.setSubject("MS Club of SLIIT - Interview");
        microsoftTeamsMeetingDetails.setBody(body);
        microsoftTeamsMeetingDetails.setStart(start);
        microsoftTeamsMeetingDetails.setEnd(end);
        microsoftTeamsMeetingDetails.setLocation(location);
        microsoftTeamsMeetingDetails.setAttendees(attendees);
        microsoftTeamsMeetingDetails.setAllowNewTimeProposals(true);
        microsoftTeamsMeetingDetails.setOnlineMeeting(true);
        microsoftTeamsMeetingDetails.setOnlineMeetingProvider("teamsForBusiness");

        HttpEntity<String> entity =
                new HttpEntity<String>(new Gson().toJson(microsoftTeamsMeetingDetails).toString(), headers);

        ResponseEntity<String> response =
                restTemplate.exchange("",
                        HttpMethod.POST,
                        entity,
                        String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return scheduleMeetingTest("Meeting Scheduleed");
        } else {
            return null;
        }
    }

}
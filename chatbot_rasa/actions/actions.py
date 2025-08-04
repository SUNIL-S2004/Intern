from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
import requests
import logging
from typing import Any, Text, Dict, List

logger = logging.getLogger(__name__)

class ActionCheckStatus(Action):
    def name(self) -> Text:
        return "action_check_status"

    def run(
        self,
        dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]
    ) -> List[Dict[Text, Any]]:
        
        try:
            application_id = tracker.get_slot("application_id")
            if not application_id:
                dispatcher.utter_message(text="Please provide your application ID.")
                return []

            logger.info(f"Checking status for application ID: {application_id}")

            response = requests.get(f"http://localhost:8082/api/status?applicationId={application_id}")

            
            logger.info(f"Backend response: {response.status_code} - {response.text}")

            if response.status_code == 200:
                try:
                    data = response.json()
                    status = data.get('status')
                    if status:
                        dispatcher.utter_message(
                            text=f"Application ID {application_id} status: {status}"
                        )
                    else:
                        dispatcher.utter_message(
                            text="Status not available for this application."
                        )
                except ValueError as e:
                    logger.error(f"JSON parse error: {e}")
                    dispatcher.utter_message(
                        text="Unexpected response format from backend."
                    )
            elif response.status_code == 404:
                dispatcher.utter_message(
                    text=f"No application found with ID {application_id}."
                )
            else:
                dispatcher.utter_message(
                    text="Couldn't fetch status. Please try again later."
                )
                
        except requests.exceptions.RequestException as e:
            logger.error(f"Connection error: {e}")
            dispatcher.utter_message(
                text="Unable to connect to backend service."
            )
            
        return []

# for district 
class ActionGetDistricts(Action):
    def name(self) -> Text:
        return "action_get_districts"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        try:
            response = requests.get("http://localhost:8082/api/districts")  # Replace with your actual API
            districts = response.json()

            buttons = [
                {"title": d, "payload": f'/select_district{{"district": "{d}"}}'}
                for d in districts
            ]
            dispatcher.utter_message(text="Please choose your district:", buttons=buttons)

        except Exception as e:
            logger.error(f"Error fetching districts: {e}")
            dispatcher.utter_message(text="Couldn't fetch district list.")
        return []


# for taluk

class ActionGetTaluks(Action):
    def name(self) -> Text:
        return "action_get_taluks"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        district = tracker.get_slot("district")
        try:
            response = requests.get(f"http://localhost:8082/api/taluks?district={district}")
            taluks = response.json()

            buttons = [
                {"title": t, "payload": f'/select_taluk{{"taluk": "{t}"}}'}
                for t in taluks
            ]
            dispatcher.utter_message(text="Now choose your taluk:", buttons=buttons)

        except Exception as e:
            logger.error(f"Error fetching taluks: {e}")
            dispatcher.utter_message(text="Couldn't fetch taluks.")
        return []

# village

class ActionGetvillages(Action):
    def name(self) -> Text:
        return "action_get_village"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        taluk = tracker.get_slot("taluk")
        try:
            response = requests.get(f"http://localhost:8082/api/village?taluk={taluk}")
            villages = response.json()

            buttons = [
                {"title": z, "payload": f'/select_village{{"village": "{z}"}}'}
                for z in villages
            ]
            dispatcher.utter_message(text="Select your village:", buttons=buttons)

        except Exception as e:
            logger.error(f"Error fetching villages: {e}")
            dispatcher.utter_message(text="Couldn't fetch villages.")
        return []
# for survey and subdivision
class ActionGetSurvey(Action):
    def name(self) -> Text:
        return "action_get_survey"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        try:
            response = requests.get("http://localhost:8082/api/survey")
            surveys = response.json()

            buttons = [
                {"title": str(s), "payload": f'/select_survey{{"survey": "{s}"}}'}
                for s in surveys
            ]
            dispatcher.utter_message(text="Please choose your survey number:", buttons=buttons)

        except Exception as e:
            logger.error(f"Error fetching survey numbers: {e}")
            dispatcher.utter_message(text="Couldn't fetch survey numbers.")
        return []
class ActionGetSubdivision(Action):
    def name(self) -> Text:
        return "action_get_subdivision"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        survey = tracker.get_slot("survey")
        if not survey:
            dispatcher.utter_message(text="Please enter your survey number first.")
            return []

        try:
            response = requests.get(f"http://localhost:8082/api/subdivision?Survey={survey}")
            subdivisions = response.json()

            buttons = [
                {"title": str(s), "payload": f'/select_subdivision{{"subdivision": "{s}"}}'}
                for s in subdivisions
            ]
            dispatcher.utter_message(text="Please choose your subdivision number:", buttons=buttons)

        except Exception as e:
            logger.error(f"Error fetching subdivisions: {e}")
            dispatcher.utter_message(text="Couldn't fetch subdivisions.")
        return []

class ActionGetLandType(Action):
    def name(self) -> Text:
        return "action_get_land_type"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        try:
            # Assuming your backend endpoint is available
            response = requests.get("http://localhost:8082/api/land-type")
            land_types = response.json()

            # Create buttons for each land type
            buttons = [
                {
                    "title": land_type, 
                    "payload": f'/select_land_type{{"land_type": "{land_type}"}}'
                }
                for land_type in land_types
            ]
            
            dispatcher.utter_message(
                text="Please select the type of land:",
                buttons=buttons
            )

        except Exception as e:
            logger.error(f"Error fetching land types: {e}")
            dispatcher.utter_message(text="Sorry, I couldn't fetch the land types at the moment.")
        
        return []


class ActionShowSummary(Action):
    def name(self) -> Text:
        return "action_show_summary"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        district = tracker.get_slot("district")
        taluk = tracker.get_slot("taluk")
        village = tracker.get_slot("village")
        survey = tracker.get_slot("survey")
        subdivision = tracker.get_slot("subdivision")
        land_type = tracker.get_slot("land_type")

        message = (
            f"📋 Here's the summary of your land application:\n\n"
            f"📍 District: {district}\n🏛️ Taluk: {taluk}\n📌 Village: {village}\n"
            f"🏡 Land Type: {land_type}\n"
            f"🧾 Survey No: {survey}\n🔢 Subdivision No: {subdivision}\n\n"
            "Do you want to submit this application?"
        )

        buttons = [
            {"title": "✅ Yes", "payload": "/submit_application"},
            {"title": "❌ No", "payload": "/deny"}
        ]

        dispatcher.utter_message(text=message, buttons=buttons)
        return []


class ActionSubmitApplication(Action):
    def name(self) -> Text:
        return "action_submit_application"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        district = tracker.get_slot("district")
        taluk = tracker.get_slot("taluk")
        village = tracker.get_slot("village")
        survey = tracker.get_slot("survey")
        subdivision = tracker.get_slot("subdivision")
        land_type = tracker.get_slot("land_type")

        payload = {
            "district": district,
            "taluk": taluk,
            "village": village,
            "survey": survey,
            "subdivision": subdivision,
            "landType": land_type  
        }

        try:
            response = requests.post("http://localhost:8082/api/save", json=payload)

            if response.status_code == 200:
                data = response.json()
                application_id = data.get("applicationId", "N/A")

                dispatcher.utter_message(
                    text=f"✅ Your application has been submitted successfully!\n\n"
                         f"📍 District: {district}\n🏛️ Taluk: {taluk}\n📌 Village: {village}\n"
                         f"🏡 Land Type: {land_type}\n"
                         f"🧾 Survey: {survey}, Subdivision: {subdivision}\n"
                         f"🆔 Application ID: {application_id}"
                )

            else:
                dispatcher.utter_message(text="❌ Error while saving your application. Please try again.")

        except Exception as e:
            logger.error(f"❗ Error submitting application: {e}")
            dispatcher.utter_message(text="🚫 Could not connect to the backend service.")

        return []

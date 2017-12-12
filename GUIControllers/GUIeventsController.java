import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Controller class for GUIevents.fxml. Handles all ActionEvent and
 * MouseEvents that occur on the events page in the application. *
 * 
 * @author Group#2 *
 */

public class GUIeventsController implements Initializable
{
	private Event eventToAdd;
	private EventList eventList = new EventList();
	private static final String FILENAME = "EventList.txt";
	private FileReaderWriter eventFile = new FileReaderWriter(FILENAME);
	
	private ObservableList<String> typeChoices = FXCollections.observableArrayList("Lecture","Seminar","Workshop","Journey");
	private ObservableList<String> categoryChoices =FXCollections.observableArrayList("Astrology","Therapy","Fortune-telling");
	private ObservableList<Event>  events = FXCollections.observableArrayList();
	
	@FXML private BorderPane eventsPage = new BorderPane();
	
	@FXML private Button btnCreateEvent,btnDeleteEvent,btnCancelEditEvent,btnEditEvent,btnClearTextFields,
							btnSearchEvents,btnSaveEventEditChanges,btnClearEditEventTextFields,btnAddMemberToEvent,
							btnRemoveMemberFromEvent;
	
	@FXML private TextField tfEnterSearchKeywords,tfEventEndTime,tfShowEventPrice,tfShowEventNumberOfTickets,tfShowEventEndTime,
							tfEventNumberOfTickets,tfEventPrice,tfShowEventDiscount,tfEnterEventTitle,tfEventStartTime,
							tfEventDiscount,tfShowEventTicketsRemaining,tfShowEventStartTime,tfSelectedEvent,tfShowEventTitle;

	@FXML private ComboBox<String> cbEventCategory,cbEventSearchCriteria,cbShowEventCategory,cbEventType,cbShowEventLecturer,cbShowEventType;
	
	@FXML private TableView<Event> eventsTable;
	
	@FXML private TableColumn<Event, String> tcEventTitle,tcEventType,tcEventCategory,
									tcEventStartTime,tcEventEndTime,tcEventDuration,tcEventPrice,tcEventDiscount,tcEventNumberOfTickets,
									tcTicketsRemaining,tcEventLecturer;
	
	@FXML private TableColumn<Event, LocalDate> tcEventStartDate,tcEventEndDate;
	
	@FXML private Label lblEventCount;
	
	@FXML private ListView<?> lvEventSearchResults,lvMemberList,lvMembersAddedToEvent;
	
	@FXML private CheckBox cbSelectAllMembersToAdd,cbSelectAllMembersToRemove;
	
	@FXML private DatePicker dpShowEventEndDate,dpShowEventStartDate,dpEventStartDate,dpEventEndDate;
	
	@FXML private HBox hboxEventEditOptions;	
	
	@FXML private ScrollPane spEventsTableScrollPane;
	
	@FXML private TitledPane tpShowEventsPane,tpShowSearchEventsPane,tpShowCreateEventPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		eventsPage.setMaxHeight(Double.MAX_VALUE);
		eventsPage.setMaxWidth(Double.MAX_VALUE);	
		
		hboxEventEditOptions.setVisible(false);			
		
		/*
		dpShowEventStartDate.setDisable(true);
		dpShowEventEndDate.setDisable(true);		
		
		tfShowEventTitle.setEditable(false);		
		tfShowEventStartTime.setEditable(false);
		tfShowEventEndTime.setEditable(false);
		tfShowEventNumberOfTickets.setEditable(false);
		tfShowEventTicketsRemaining.setEditable(false);
		tfShowEventPrice.setEditable(false);
		tfShowEventDiscount.setEditable(false);
		*/
		//initialize combobox
		
		cbEventType.setItems(typeChoices);
		cbEventType.getSelectionModel().select(0);
		cbEventCategory.setItems(categoryChoices);
		cbEventCategory.getSelectionModel().select(0);
		
		//initialize table
		tcEventTitle.setCellValueFactory(new PropertyValueFactory<Event,String>("eventName"));
		//tcEventType.setCellValueFactory(new PropertyValueFactory<Event,?>("eventType"));
		//tcEventCategory.setCellValueFactory(new PropertyValueFactory<Event,?>("eventCategory"));
		tcEventStartDate.setCellValueFactory(new PropertyValueFactory<Event,LocalDate>("eventStartDate"));
		tcEventEndDate.setCellValueFactory(new PropertyValueFactory<Event,LocalDate>("eventEndDate"));
		tcEventStartTime.setCellValueFactory(new PropertyValueFactory<Event, String>("startTime"));
		tcEventEndTime.setCellValueFactory(new PropertyValueFactory<Event, String>("endTime"));
		tcEventPrice.setCellValueFactory(new PropertyValueFactory<Event, String>("price"));
		tcEventDiscount.setCellValueFactory(new PropertyValueFactory<Event, String>("discount"));
		tcEventNumberOfTickets.setCellValueFactory(new PropertyValueFactory<Event, String>("maxMembers"));
		
		tcEventTitle.setStyle("-fx-alignment: CENTER;");
		tcEventStartDate.setStyle("-fx-alignment: CENTER;");
		tcEventEndDate.setStyle("-fx-alignment: CENTER;");
		tcEventStartTime.setStyle("-fx-alignment: CENTER;");
		tcEventEndTime.setStyle("-fx-alignment: CENTER;");
		tcEventPrice.setStyle("-fx-alignment: CENTER;");
		tcEventDiscount.setStyle("-fx-alignment: CENTER;");
		tcEventNumberOfTickets.setStyle("-fx-alignment: CENTER;");		
		
		//spEventsTableScrollPane.setStyle("-fx-font-size: 13px;");
		
		btnEditEvent.setDisable(true);
		btnDeleteEvent.setDisable(true);
		
		
		
		try
		{
		   eventsTable.setItems(getList());
		}
		catch (FileNotFoundException e)
		{
		   e.printStackTrace();
		}
		catch (ParseException e)
		{
		   e.printStackTrace();
		}		
		
		eventsTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 1) {
				tpShowEventsPane.setExpanded(true);
				btnEditEvent.setDisable(false);
				btnDeleteEvent.setDisable(false);				
			}
		});
		
		/*tpShowCreateEventPane.setOnMouseClicked((MouseEvent event) -> {
			if(event.getClickCount() == 1) {
				tpShowEventsPane.setExpanded(false);
			}
		});
		
		tpShowSearchEventsPane.setOnMouseClicked((MouseEvent event) -> {
			if(event.getClickCount() == 1) {
				tpShowEventsPane.setExpanded(false);
			}
		}); */
	}	
	
	public void showEventDetailsFromTable()
	{
		
	}
	
	public void showEventDetailsFromListView()
	{
		
	}

    @FXML
    void createEvent(ActionEvent event) throws ParseException, CloneNotSupportedException, IOException
    {
    	String eventName = tfEnterEventTitle.getText();
    	//DatePicker datePicker = new DatePicker();
    	LocalDate localStartDate = dpEventStartDate.getValue();
    	LocalDate localEndDate = dpEventEndDate.getValue();
    	
    	String startTime = tfEventStartTime.getText();
    	String endTime = tfEventEndTime.getText();
    	double price = Double.parseDouble(tfEventPrice.getText());
    	double discount = Double.parseDouble(tfEventDiscount.getText());
    	int maxMembers = Integer.parseInt(tfEventNumberOfTickets.getText());
         	
    	Event eventNew = new Event(eventName,localStartDate,startTime,localEndDate,endTime,maxMembers,price,discount);
    	eventList.addEventToList(eventNew);
    	eventFile.writeEventTextFile(eventList);
    	eventsTable.getItems().add(eventNew);   	
    	
    	clearCreateEventTextFields(event);
    }
    
    public ObservableList<Event> getList() throws FileNotFoundException, ParseException
    {
       eventList = eventFile.readEventsTextFile();
       
       for ( int i=0; i<eventList.size(); i++)
       {
          events.add(new Event(eventList.getEvent(i).getEventName(),eventList.getEvent(i).getEventStartDate(),eventList.getEvent(i).getStartTime(),eventList.getEvent(i).getEventEndDate(),eventList.getEvent(i).getEndTime(),eventList.getEvent(i).getMaxMembers(),eventList.getEvent(i).getPrice(),eventList.getEvent(i).getDiscount()));
       }
       return events;
    }

    @FXML
    void clearCreateEventTextFields(ActionEvent event) 
    {
    	tfEnterEventTitle.setText("");
    }

    @FXML
    void searchEvents(ActionEvent event) 
    {
    	System.out.println("Search event");
    }

    @FXML
    void editEvent(ActionEvent event) 
    {
    	System.out.println("Edit event");
    	hboxEventEditOptions.setVisible(true);

		tfShowEventTitle.setEditable(true);		
		dpShowEventStartDate.setEditable(true);
		dpShowEventEndDate.setEditable(true);
		tfShowEventStartTime.setEditable(true);
		tfShowEventEndTime.setEditable(true);
		tfShowEventNumberOfTickets.setEditable(true);
		tfShowEventTicketsRemaining.setEditable(true);
		tfShowEventDiscount.setEditable(true);

    }
    
    @FXML
    void saveEditEventChanges(ActionEvent event) 
    {
    	System.out.println("Save edit changes");
    }

    @FXML
    void clearEditEventTextFields(ActionEvent event) 
    {
    	System.out.println("Clear edit event text fields");
    }

    @FXML
    void cancelEditEvent(ActionEvent event) 
    {
    	System.out.println("Cancel edit event");
    	hboxEventEditOptions.setVisible(false);
    	
		tfShowEventTitle.setEditable(false);		
		dpShowEventStartDate.setEditable(false);
		dpShowEventEndDate.setEditable(false);
		tfShowEventStartTime.setEditable(false);
		tfShowEventEndTime.setEditable(false);
		tfShowEventNumberOfTickets.setEditable(false);
		tfShowEventTicketsRemaining.setEditable(false);
		tfShowEventDiscount.setEditable(false);
    }

    @FXML
    void deleteEvent(ActionEvent event) 
    {
    	System.out.println("Delete event");    	
    }

    @FXML
    void addMemberToEvent(ActionEvent event) 
    {
    	System.out.println("Add member to event");
    }

    @FXML
    void removeMemberFromEvent(ActionEvent event) 
    {
    	System.out.println("Remove member from event");
    }
}
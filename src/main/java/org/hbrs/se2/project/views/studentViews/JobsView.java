package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.control.ReportsControl;
import org.hbrs.se2.project.control.RatingControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.RatingDTO;
import org.hbrs.se2.project.dtos.impl.RatingDTOImpl;
import org.hbrs.se2.project.dtos.impl.ReportsDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Student - Jobs list View
 */
@Route(value = Globals.Pages.JOBS_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Jobs")
public class JobsView extends Div {
    private final CommonUIElementProvider ui;
    private final UserControl userControl;
    private final ReportsControl reportsControl;
    private final RatingControl ratingControl;
    // interactive search field
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button(getTranslation("view.job.button.search"));
    private Button report;
    private Button rate;
    private Button noReport;
    private Button noRate;
    Button confirm = new Button("Bewertung abgeben");
    private final Span one = new Span();
    private final Span two = new Span();
    private final Span three = new Span();
    private final Span four = new Span();
    private final Span five = new Span();
    private final HorizontalLayout buttons = new HorizontalLayout();
    // Create a Grid bound to the list
    private final Grid<JobDTO> grid = new Grid<>();

    public JobsView(JobControl jobControl, UserControl userControl, CommonUIElementProvider ui, ReportsControl reportsControl, RatingControl ratingControl) {
        this.userControl = userControl;
        this.ui = ui;
        this.reportsControl = reportsControl;
        this.ratingControl = ratingControl;

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        searchField.setClearButtonVisible(true); // possibility to delete filter words

        // changing width of textField, buttonFilter and buttonAllJobs to improve on usability
        searchField.setWidth("450px");
        searchButton.setWidth("15%");
        Button buttonAllJobs = new Button("Alle Jobs");
        buttonAllJobs.setWidth("15%");

        searchField.setPlaceholder(getTranslation("view.job.text.search"));

        HorizontalLayout topLayout = new HorizontalLayout();

        Stream.of(searchField, searchButton, buttonAllJobs).forEach(element -> element.getStyle().set("margin-center", "auto"));

        // Center Alignment
        topLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // adding TextField and 2 buttons
        topLayout.add(searchField);
        topLayout.add(searchButton);
        topLayout.add(buttonAllJobs);

        layout.add(topLayout);
        layout.add(new Label());
        layout.setWidth("100%");

        // Header
        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true).setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(job -> new Text(jobControl.getCompanyOfJob(job))).setHeader("Unternehmen");
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true);
        grid.setAllRowsVisible(true);
        searchField.addKeyPressListener(Key.ENTER, event -> searchButton.clickInClient());
        // pass relevant job list to grid
        searchButton.addClickListener(event -> {grid.setItems(jobControl.getJobsMatchingKeyword(searchField.getValue())); searchField.clear();});
        // set items details renderer
        grid.setItemDetailsRenderer(new ComponentRenderer<>(job -> {
            buttons.removeAll();
            Button contact = new Button("Kontaktieren");
            report = new Button("Melden");
            rate = new Button("Bewerten");
            noReport = new Button("Bereits gemeldet");
            noRate = new Button("Bereits bewertet");
            VerticalLayout vLayout = new VerticalLayout();
            FormLayout formLayout = new FormLayout();

            final TextField companyName = new TextField(getTranslation("view.job.text.company"));
            final Span rating = getRating(job.getCompanyid());
            final TextField jobLocation = new TextField(getTranslation("view.job.text.location"));
            final TextField companyContactDetails = new TextField(getTranslation("view.job.text.contactDetails"));
            final TextArea jobDescription = new TextArea(getTranslation("view.job.text.description"));

            // set all fields with job details
            companyName.setValue(jobControl.getCompanyOfJob(job));
            jobLocation.setValue(job.getLocation());
            companyContactDetails.setValue(job.getContactdetails());
            jobDescription.setValue(job.getDescription());

            // add textFields to FormLayout
            formLayout.add(companyName, rating, jobLocation, companyContactDetails, jobDescription);
            Stream.of(companyName, jobLocation, companyContactDetails, jobDescription).forEach(
                    field -> field.setReadOnly(true)
            );

            contact.addClickListener(event -> ui.makeConversationDialogStudent(job.getCompanyid(), userControl.getStudentProfile(
                    userControl.getCurrentUser().getUserid()).getStudentid(), job.getJobid()));
            buttons.add(contact);

            noReport.setEnabled(false);
            noRate.setEnabled(false);

            if(!reportsControl.studentHasReportedCompany(job.getCompanyid(), userControl.getStudentProfile(
                    userControl.getCurrentUser().getUserid()).getStudentid())) {
                report.addClickListener(event -> reportDialog(job.getCompanyid(), userControl.getStudentProfile(
                        userControl.getCurrentUser().getUserid()).getStudentid()));
                buttons.add(report);
            } else {
                buttons.add(noReport);
            }

            if(!ratingControl.studentHasRatedCompany(job.getCompanyid(),  userControl.getStudentProfile(
                    userControl.getCurrentUser().getUserid()).getStudentid())) {
                rate.addClickListener(event -> rateDialog(job));
                buttons.add(rate);
            } else {
                buttons.add(noRate);
            }

            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
            formLayout.setColspan(jobDescription, 2);

            vLayout.add(formLayout, buttons);
            return vLayout;
        }));

        grid.setItems(jobControl.getAllJobs());

        // buttonAllJobs will show all Job Ads without any filter

        buttonAllJobs.addClickListener(event -> {
            List<JobDTO> jobDetails = jobControl.getAllJobs();
            grid.setItems(jobDetails);
        });

        // Adding column Borders and Row Stripes for better visibility
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        /*
        Adding a text with the number of available Jobs underneath the grid
        To achieve that I used a workaround instead of working with the size of the grid
        */

        List<JobDTO> jobDetails = jobControl.getAllJobs();

        H2 jobCountText = new H2("Es sind momentan " + jobDetails.size() + " Jobs für dich verfügbar!");

        jobCountText.getElement().getStyle().set("font-size","15px");
        jobCountText.getElement().getStyle().set("text-align","center");

        // adding text, topLayout, grid and the text for showing the total number of jobs available

        /*
        with introductionText() below the career page will show information and help the user
        guide through the option of either filtering the job ads or showing all available job ads
        */
        add(ui.introductionText("Willkommen auf deiner Karriereseite!", "Du kannst nach Jobs filtern oder dir direkt alle anzeigen lassen!"));
        add(topLayout);
        add(grid);
        add(jobCountText);
    }

    private Span getRating(int companyid){
        Span rating = new Span("Unternehmensreputation: ");
        Float avg = ratingControl.getRating(companyid);
        if (avg == null) {
            avg = 0.0F;
        }
        for (int i = 0; i < 5; i++) {
            if (avg >= 1) {
                rating.add(new Icon(VaadinIcon.STAR));
            } else if (avg > 0.25 && avg < 0.75) {
                rating.add(new Icon(VaadinIcon.STAR_HALF_LEFT_O));
            } else {
                rating.add(new Icon(VaadinIcon.STAR_O));
            }
            avg = avg - 1;
        }
        return rating;
    }

    private void reportDialog(int companyid, int studentid) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        VerticalLayout layout = new VerticalLayout(new Text("Hier kannst du ein Unternehmen wegen Fehlverhaltens oder schlechten Erfahrungen melden. " +
                "Gib bitte eine kurze Beschreibung des zu Grunde liegenden Sachverhaltes an."));
        TextArea content = new TextArea("Grund der Meldung:");
        content.setMaxLength(1000);
        content.setWidthFull();
        content.setHeight("200px");
        content.setRequired(true);
        HorizontalLayout reportButtons = new HorizontalLayout();
        Button close = new Button("Abbrechen");
        close.addClickListener(event -> dialog.close());
        Button send = new Button("Senden");
        send.addClickListener(event -> {
            if (!content.isEmpty()) {
                ui.makeYesNoDialog("Anfrage abschicken?", click -> {
                    Binder<ReportsDTOImpl> binder = new BeanValidationBinder<>(ReportsDTOImpl.class);
                    binder.setBean(new ReportsDTOImpl(companyid, studentid, content.getValue()));
                    reportsControl.createReport(binder.getBean());
                    buttons.replace(report, noReport);
                    dialog.close();
                    ui.throwNotification("Unternehmen erfolgreich gemeldet.");
                });
            } else {
                ui.makeDialog("Gib bitte einen Grund für die Meldung an.");
            }
        });
        reportButtons.add(close, send);
        layout.add(content, reportButtons);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, reportButtons);
        dialog.add(layout);
        dialog.open();
    }

    private void rateDialog(JobDTO job){
        Dialog dialog = new Dialog();
        AtomicInteger rating = new AtomicInteger();
        Button close = new Button("Abbrechen");
        close.addClickListener(event -> dialog.close());
        updateStars(ratingStars(0));
        HorizontalLayout starsLayout = new HorizontalLayout(one, two, three, four, five);
        confirm.addClickListener(event -> ui.makeYesNoDialog("Möchtest du die Bewertung so einreichen?", click -> {
            RatingDTO ratingDTO = new RatingDTOImpl(userControl.getStudentProfile(userControl.getCurrentUser().getUserid()).getStudentid(),
                    job.getCompanyid(), rating.get());
            ratingControl.createRating(ratingDTO);
            buttons.replace(rate, noRate);
            dialog.close();
            ui.throwNotification("Unternehmen erfolgreich bewertet.");
        }));
        confirm.setEnabled(false);
        HorizontalLayout rateButtons = new HorizontalLayout(close, confirm);
        VerticalLayout layout = new VerticalLayout(new Text("Wie zufrieden bist du mit dem Unternehmen von einem Stern (nicht zufrieden) bis fünf Sternen (sehr zufrieden)?"),
                starsLayout, rateButtons);
        layout.setWidth("400px");
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, starsLayout, rateButtons);
        one.addClickListener(event -> {
            rating.set(1);
            updateStars(ratingStars(1));
        });
        two.addClickListener(event -> {
            rating.set(2);
            updateStars(ratingStars(2));
        });
        three.addClickListener(event -> {
            rating.set(3);
            updateStars(ratingStars(3));
        });
        four.addClickListener(event -> {
            rating.set(4);
            updateStars(ratingStars(4));
        });
        five.addClickListener(event -> {
            rating.set(5);
            updateStars(ratingStars(5));
        });
        dialog.add(layout);
        dialog.open();
    }

    private List<AtomicReference<Icon>> ratingStars(int rating){
        AtomicInteger stars = new AtomicInteger(rating);
        List<AtomicReference<Icon>> references = new ArrayList<>();
        AtomicReference<Icon> oneStar = new AtomicReference<>(new Icon(VaadinIcon.STAR_O));
        AtomicReference<Icon> twoStars = new AtomicReference<>(new Icon(VaadinIcon.STAR_O));
        AtomicReference<Icon> threeStars = new AtomicReference<>(new Icon(VaadinIcon.STAR_O));
        AtomicReference<Icon> fourStars = new AtomicReference<>(new Icon(VaadinIcon.STAR_O));
        AtomicReference<Icon> fiveStars = new AtomicReference<>(new Icon(VaadinIcon.STAR_O));
        Stream.of(oneStar, twoStars, threeStars, fourStars, fiveStars).forEach(star -> {
            if (stars.getAndDecrement() >= 1) {
                star.set(new Icon(VaadinIcon.STAR));
            } else {
                star.set(new Icon(VaadinIcon.STAR_O));
            }
            references.add(star);
        });
        return references;
    }

    private void updateStars(List<AtomicReference<Icon>> icons){
        Stream.of(one, two, three, four, five).forEach(star -> {
            star.removeAll();
            star.add(icons.remove(0).get());
        });
        confirm.setEnabled(true);
    }

}

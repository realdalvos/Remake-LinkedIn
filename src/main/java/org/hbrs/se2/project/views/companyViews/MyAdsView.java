package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Stream;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {

    @Autowired
    private CommonUIElementProvider ui;

    private final JobControl jobControl;
    private final UserControl userControl;

    private final TextField title = new TextField(getTranslation("view.job.text.title"));
    private final TextArea description = new TextArea(getTranslation("view.job.text.description"));
    private final TextField salary = new TextField(getTranslation("view.job.text.salary"));
    private final TextField location = new TextField(getTranslation("view.job.text.location"));
    private final TextField contactdetails = new TextField(getTranslation("view.job.text.contactDetails"));

    private final Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);
    private final ModelMapper mapper = new ModelMapper();

    public MyAdsView(JobControl jobControl, UserControl userControl) {
        this.jobControl = jobControl;
        this.userControl = userControl;

        title.setMaxWidth("33%");
        title.setMaxLength(75);
        title.setWidthFull();
        salary.setMaxWidth("33%");
        salary.setWidthFull();
        location.setMaxWidth("33%");
        location.setMaxLength(64);
        location.setWidthFull();
        description.setMaxWidth("55%");
        description.setMaxLength(1024);
        description.setWidthFull();
        contactdetails.setMaxWidth("30%");
        contactdetails.setMaxLength(100);
        contactdetails.setWidthFull();

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobControl.getAllCompanyJobs(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setAllRowsVisible(true);
        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true).setWidth("20%");
        grid.addColumn(JobDTO::getDescription).setHeader(getTranslation("view.job.text.description")).setWidth("30%");
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true).setWidth("15%");
        grid.addColumn(JobDTO::getLocation).setHeader(getTranslation("view.job.text.location")).setWidth("15%");
        grid.addComponentColumn(JobDTO -> {
            Button deleteButton = new Button(getTranslation("view.button.delete"));
            deleteButton.addClickListener(e -> ui.makeYesNoDialog("Möchtest du dieses Jobangebot wirklich löschen?",
                    event -> {
                        jobControl.deleteJob(JobDTO.getJobid());
                        grid.setItems(jobControl.getAllCompanyJobs(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid()));
                        ui.throwNotification("Jobanzeige erfolgreich gelöscht.");
                    }));
            return deleteButton;
        });

        // Detail renderer for more information
        grid.setItemDetailsRenderer(new ComponentRenderer<>(jobDTO -> {
            binder.setBean(mapper.map(jobDTO, JobDTOImpl.class));
            binder.forField(salary)
                    .withValidator(validation -> salary.getValue().matches("-?\\d+"), "Kein gültiges Gehalt")
                    .withConverter(new StringToIntegerConverter(""))
                    .bind(JobDTOImpl::getSalary, JobDTOImpl::setSalary);
            binder.bindInstanceFields(this);
            FormLayout layout = new FormLayout();
            Button edit = new Button("Bearbeiten");
            Stream.of(title, description, salary, location, contactdetails).forEach(
                    field -> {
                        field.setReadOnly(true);
                        layout.add(field);
                    }
            );
            HorizontalLayout buttons = new HorizontalLayout(edit);
            layout.add(buttons);
            edit.addClickListener(editEvent -> {
                Stream.of(title, description, salary, location, contactdetails).forEach(
                        field -> field.setReadOnly(false)
                );
                buttons.remove(edit);
                Button cancel = new Button("Abbrechen");
                Button save = new Button("Speichern");
                buttons.add(cancel, save);
                save.addClickListener(saveEvent -> {
                    if (binder.isValid()) {
                        ui.makeConfirm("Möchtest du die Änderungen an dieser Jobanzeige speichern?",
                                event -> {
                                    jobControl.createNewJobPost(binder.getBean());
                                    grid.setItems(jobControl.getAllCompanyJobs(userControl.getCompanyProfile(userControl.getCurrentUser().getUserid()).getCompanyid()));
                                    ui.throwNotification("Jobanzeige erfolgreich geändert.");
                                });
                    } else {
                        ui.makeDialog("Überprüfe bitte deine Angaben auf Korrektheit");
                    }
                });
                cancel.addClickListener(saveEvent -> {
                    Stream.of(title, description, salary, location, contactdetails).forEach(
                            field -> field.setReadOnly(true)
                    );
                    buttons.remove(cancel, save);
                    buttons.add(edit);
                });
            });
            layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));
            layout.setColspan(title, 2);
            layout.setColspan(description, 4);
            layout.setColspan(salary, 1);
            layout.setColspan(location, 2);
            layout.setColspan(contactdetails, 2);
            return layout;
        }));
        grid.setHeight("100%");
        H3 title = new H3(" Übersicht über deine aktuellen Stellenausschreibungen");
        title.getElement().getStyle().set("color", "#f2a6b4");
        title.getElement().getStyle().set("text-align", "center");
        add(title);
        add(grid);
        setHeight("100%");
    }

}

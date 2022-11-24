package org.hbrs.se2.project.views.companyViews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.JobControl;
import org.hbrs.se2.project.dtos.JobDTO;
import org.hbrs.se2.project.dtos.impl.JobDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.util.Utils;
import org.hbrs.se2.project.views.AppView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

/**
 * Company - My Ads List View
 */
@Route(value = Globals.Pages.MYADS_VIEW, layout = AppView.class)
@PageTitle("Meine Stellen")
public class MyAdsView extends Div {

    @Autowired
    private CommonUIElementProvider ui;

    private final JobControl jobControl;

    private final TextField title = new TextField(getTranslation("view.job.text.title"));
    private final TextArea description = new TextArea(getTranslation("view.job.text.description"));
    private final TextField salary = new TextField(getTranslation("view.job.text.salary"));
    private final TextField location = new TextField(getTranslation("view.job.text.location"));
    private final TextField contactdetails = new TextField(getTranslation("view.job.text.contactDetails"));

    private final Binder<JobDTOImpl> binder = new BeanValidationBinder<>(JobDTOImpl.class);
    private final ModelMapper mapper = new ModelMapper();

    public MyAdsView(JobControl jobControl){
        this.jobControl = jobControl;

        Grid<JobDTO> grid = new Grid<>();

        grid.setItems(jobControl.getAllCompanyJobs(jobControl.getCompanyByUserid(Utils.getCurrentUser().getUserid()).getCompanyid()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setHeightByRows(true);
        grid.addColumn(JobDTO::getTitle).setHeader(getTranslation("view.job.text.title")).setSortable(true);
        grid.addColumn(JobDTO::getDescription).setHeader(getTranslation("view.job.text.description"));
        grid.addColumn(JobDTO::getSalary).setHeader(getTranslation("view.job.text.salary")).setSortable(true);
        grid.addComponentColumn(JobDTO -> {
            Button deleteButton = new Button(getTranslation("view.job.button.delete"));
            deleteButton.addClickListener(e -> ui.makeYesNoDialog("Möchten Sie dieses Jobangebot wirklich löschen?", delete(JobDTO.getJobid())));
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
            layout.add(edit);
            edit.addClickListener(editEvent -> {
                Stream.of(title, description, salary, location, contactdetails).forEach(
                        field -> field.setReadOnly(false)
                );
                layout.remove(edit);
                Button save = new Button("Speichern");
                layout.add(save);
                save.addClickListener(saveEvent -> {
                    if (binder.isValid()) {
                        ui.makeConfirm("Möchten Sie die Änderungen an diesem Jobangebot speichern?", confirm(binder.getBean()));
                    } else {
                        ui.makeDialog("Überprüfen Sie bitte Ihre Angaben auf Korrektheit");
                    }
                });
            });

            return layout;
        }));

        add(grid);
    }

    private Button confirm(JobDTO job) {
        Button save = new Button();
        save.addClickListener(event -> {jobControl.createNewJobPost(job);UI.getCurrent().getPage().reload();});
        return save;
    }

    private Button delete(int jobid) {
        Button delete = new Button();
        delete.addClickListener(event -> {jobControl.deleteJob(jobid);UI.getCurrent().getPage().reload();});
        return delete;
    }

}

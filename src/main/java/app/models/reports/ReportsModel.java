package app.models.reports;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.reports.LoanApplicationReportEntity;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ReportsModel extends MainModel {
    ErrorLogger logger = new ErrorLogger();
    protected ObservableList<LoanApplicationReportEntity> fetchLoanApplicantReportData(Date start, Date end) {
        ObservableList<LoanApplicationReportEntity> data = new ObservableStack<>();
        try {
            String query = """
                    SELECT concat(c.firstname, " ", c.lastname, " ",c.othername) AS fullname,\s
                    loan_no, application_status, approved_amount, repayment_amount, total_payment,
                    DATE(l.date_created) AS entry_date, concat(emp.firstname, ' ', emp.lastname) AS super_name
                    FROM loans AS l
                    INNER JOIN customer_data AS c
                    USING(customer_id)\s
                    JOIN group_supervisors AS gs
                    ON l.loan_no = gs.loan_id
                    JOIN employees AS emp
                    ON gs.emp_id = emp.work_id
                    WHERE DATE(l.date_created) BETWEEN ? AND ?
                    ORDER BY entry_date;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);
            resultSet = preparedStatement.executeQuery();
            AtomicInteger counter = new AtomicInteger(0);
            while (resultSet.next()) {
                String name = resultSet.getString("fullname");
                String loanNo = resultSet.getString("loan_no");
                String status = resultSet.getString("application_status");
                double disbursed = resultSet.getDouble("approved_amount");
                double repayment = resultSet.getDouble("repayment_amount");
                double paid = resultSet.getDouble("total_payment");
                Date date = resultSet.getDate("entry_date");
                String supervisor = resultSet.getString("super_name");
                data.add(new LoanApplicationReportEntity(counter.incrementAndGet(), name, loanNo, status, supervisor, disbursed, repayment, paid, date));
            }
            resultSet.close();
            preparedStatement.close();
            getConnection().close();
        }catch (SQLException ex){}
        return data;
    }





}

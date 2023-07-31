package app.models.humanResource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.MainModel;

import java.sql.SQLException;

public class HumanResourceModel extends MainModel {
    public int getTotalEmployeesCount() {
        int count = 0;
        try {
            String query = "SELECT count(*) FROM employees";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }catch (SQLException e) {e.printStackTrace();}
        return count;
    }

    protected int addNewEmployee(EmployeesData employeesData) {
        int flag = 0;


        return flag;
    }

}//end of class....

package app.repositories.operations;

import io.github.palexdev.materialfx.controls.MFXCheckbox;

public class PermissionsEntity {
//    id, module_name, alias, descriptions, is_active
//    operation_id, module_id, operation_name, alias, description, is_active

    private int id, operation_id, module_id, roleId, modified_by;
    private String module_name, alias, description, operationName, operationAlias, operationDescription;
    boolean isAllowed, operationActive;
    MFXCheckbox checkbox = new MFXCheckbox("YES");

    public PermissionsEntity(int id, String module_name, String alias, String description, boolean isAllowed) {
        this.id = id;
        this.module_name = module_name;
        this.alias = alias;
        this.description = description;
        this.isAllowed = isAllowed;
        modifyButton();
    }

    public PermissionsEntity(int operation_id, int module_id, String operationName, String operationAlias, String operationDescription, boolean operationActive) {
        this.operation_id = operation_id;
        this.module_id = module_id;
        this.operationName = operationName;
        this.operationAlias = operationAlias;
        this.operationDescription = operationDescription;
        this.operationActive = operationActive;
        modifyButton();
    }
    public PermissionsEntity(int module_id, int roleId, int operation_id, boolean isAllowed, int modified_by) {
        this.module_id = module_id;
        this.roleId = roleId;
        this.operation_id = operation_id;
        this.isAllowed = isAllowed;
        this.modified_by = modified_by;
        modifyButton();
    }


    public PermissionsEntity() {

    }

    private void modifyButton() {
        checkbox.setStyle("-fx-font-size:11px; -fx-font-family:poppins medium; -fx-font-weight: bold; -fx-width:10px;");
        checkbox.setSelected(isAllowed);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean is_active) {
        this.isAllowed = is_active;
    }

    public MFXCheckbox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(MFXCheckbox checkbox) {
        this.checkbox = checkbox;
    }

    public int getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(int operation_id) {
        this.operation_id = operation_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationAlias() {
        return operationAlias;
    }

    public void setOperationAlias(String operationAlias) {
        this.operationAlias = operationAlias;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public boolean getOperationActive() {
        return operationActive;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public boolean isOperationActive() {
        return operationActive;
    }

    public void setOperationActive(boolean operationActive) {
        this.operationActive = operationActive;
    }
}//END OF CLASS...

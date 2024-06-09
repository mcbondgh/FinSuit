package app.repositories.operations;

import io.github.palexdev.materialfx.controls.MFXCheckbox;

import java.sql.Timestamp;

public class PermissionsEntity {
    private int id, module_id, operation_id, control_id, role_id, permission_id, modified_by;
    private String module_name, alias, descriptions, operation_name;
   private  boolean allowPermission, allowModule;
   Timestamp date_modified;

    private MFXCheckbox checkbox = new MFXCheckbox("YES");

    public PermissionsEntity() {
    }

    //this constructor is for loading all permissions;
    public PermissionsEntity(int operation_id, int module_id, String operation_name, String alias, String description, boolean is_active) {
        this.operation_id = operation_id;
        this.module_id = module_id;
        this.operation_name = operation_name;
        this.alias = alias;
        this.descriptions = description;
        allowPermission = is_active;
    }

    //this constructor is for loading all module variables from the database;
    public  PermissionsEntity(int id, String module_name, String alias, String descriptions, boolean is_active) {
        this.id = id;
        this.module_name = module_name;
        this.alias = alias;
        this.descriptions = descriptions;
        this.allowModule = is_active;
    }

    //this constructor when invoked shall load all access_control table data
    public PermissionsEntity(int control_id, int module_id, boolean allowModule, int permission_id, boolean allowPermission, int modified_by ) {
        this.control_id = control_id;
        this.module_id = module_id;
        this.allowModule = allowModule;
        this.allowPermission = allowPermission;
        this.permission_id = permission_id;
        this.modified_by = modified_by;
        modifyButton();
    }

    public PermissionsEntity(int role_id, String module_name, boolean allowModule) {
        this.role_id = role_id;
        this.module_name = module_name;
        this.allowModule = allowModule;
    }

    public PermissionsEntity(int role_id, int permission_id, boolean allowPermission) {
        this.role_id = role_id;
        this.permission_id = permission_id;
        this.allowPermission = allowPermission;
    }


    private void modifyButton() {
        checkbox.setStyle("-fx-font-size:11px; -fx-font-family:poppins medium; -fx-font-weight: bold; -fx-width:10px;");
        checkbox.setSelected(allowPermission);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public int getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(int operation_id) {
        this.operation_id = operation_id;
    }

    public int getControl_id() {
        return control_id;
    }

    public void setControl_id(int control_id) {
        this.control_id = control_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
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

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getOperation_name() {
        return operation_name;
    }

    public void setOperation_name(String operation_name) {
        this.operation_name = operation_name;
    }

    public boolean isAllowPermission() {
        return allowPermission;
    }

    public void setAllowPermission(boolean allowPermission) {
        this.allowPermission = allowPermission;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public MFXCheckbox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(MFXCheckbox checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isAllowModule() {
        return allowModule;
    }

    public void setAllowModule(boolean allowModule) {
        this.allowModule = allowModule;
    }
}//END OF CLASS...

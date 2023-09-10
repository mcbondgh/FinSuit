package app.repositories.accounts;

import java.sql.Timestamp;

public class CustomersDocumentRepository {
    private int doc_id;
    private long customer_id;
    private String document_type;
    private String document_name;
   private  byte[] file_content;
    private String reason_for_upload;
    private Timestamp date_uploaded, date_modified;
    private int uploaded_by, modified_by;

    public CustomersDocumentRepository(int doc_id, long customer_id, String document_type, String document_name, byte[] file_content, String reason_for_upload, Timestamp date_uploaded, Timestamp date_modified, int uploaded_by, int modified_by) {
        this.doc_id = doc_id;
        this.customer_id = customer_id;
        this.document_type = document_type;
        this.document_name = document_name;
        this.file_content = file_content;
        this.reason_for_upload = reason_for_upload;
        this.date_uploaded = date_uploaded;
        this.date_modified = date_modified;
        this.uploaded_by = uploaded_by;
        this.modified_by = modified_by;
    }

    public CustomersDocumentRepository() {}

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public byte[] getFile_content() {
        return file_content;
    }

    public void setFile_content(byte[] file_content) {
        this.file_content = file_content;
    }

    public String getReason_for_upload() {
        return reason_for_upload;
    }

    public void setReason_for_upload(String reason_for_upload) {
        this.reason_for_upload = reason_for_upload;
    }

    public Timestamp getDate_uploaded() {
        return date_uploaded;
    }

    public void setDate_uploaded(Timestamp date_uploaded) {
        this.date_uploaded = date_uploaded;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public int getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(int uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }



}//end of class...

package com.vinfast.ui.customer;

import com.vinfast.controller.CustomerDetailController;
import com.vinfast.dto.CustomerDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailView {
    public void showDetail(CustomerDTO customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vinfast/fe/CustomerDetail.fxml"));
            Parent root = loader.load();

            CustomerDetailController controller = loader.getController();
            controller.setCustomerDetail(customer);

            Stage stage = new Stage();
            stage.setTitle("Thông tin chi tiết về khách hàng");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

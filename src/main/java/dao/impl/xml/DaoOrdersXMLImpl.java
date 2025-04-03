package dao.impl.xml;

import common.config.ConfigXML;
import common.constants.Constants;
import common.constants.ErrorConstants;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.log4j.Log4j2;
import model.Order;
import model.error.RestaurantError;
import model.error.database.GeneralDatabaseError;
import model.xml.OrderItemXML;
import model.xml.OrderXML;
import model.xml.OrdersXML;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Log4j2
public class DaoOrdersXMLImpl implements dao.DaoOrdersXML {

    private final ConfigXML config;

    @Inject
    public DaoOrdersXMLImpl(ConfigXML config) {
        this.config = config;
    }

    @Override
    public Either<RestaurantError, Integer> save(List<Order> orderList) {

        OrdersXML ordersXML = new OrdersXML();
        List<OrderXML> orderXMLList = orderList.stream()
                .map(order -> {
                    List<OrderItemXML> orderItemsXML = order.getOrderItems().stream()
                            .map(orderItem -> new OrderItemXML(orderItem.getMenuItem().getName(), orderItem.getQuantity()))
                            .toList();
                    return new OrderXML(order.getId(), orderItemsXML);
                })
                .toList();

        ordersXML.setOrders(orderXMLList);

        return write(ordersXML, orderList.get(0));
    }

    private Either<RestaurantError, Integer> write(OrdersXML ordersXML, Order order) {
        try {

            Path dataFolderPath = Path.of(config.getPathDataFolder());
            File newFile = new File(dataFolderPath.toFile(), String.format(Constants.TEXT_FORMAT, order.getCustomerId()));

                JAXBContext context = JAXBContext.newInstance(OrdersXML.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(ordersXML, newFile);

                return Either.right(ErrorConstants.OPERATION_SUCCESSFUL_CODE);

        } catch (Exception e) {
            log.error(e.getMessage());
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }


}

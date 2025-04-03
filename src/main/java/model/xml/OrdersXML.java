package model.xml;

import common.constants.Constants;
import common.constants.DbConstants;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement(name = DbConstants.ORDERS_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
public class OrdersXML {

    @XmlElement(name = Constants.ORDER)
    private List<OrderXML> orders;

}

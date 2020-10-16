package com.example.demo.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @description: SyncProducer
 * @author: leiming5
 * @date: 2020-07-31 08:47
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("xxx");
        // Specify name server addresses.
        producer.setNamesrvAddr("172.17.172.164:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 1; i++) {
            //Create a message instance, specifying topic, tag and message body.
            try {
                String messageJson = "{\"action\":3,\"subject\":\"dim_product\",\"data\":[{\"id\":\"459\",\"brand\":\"Lenovo\",\"productline\":\"PH\",\"productfamily\":\"Lenovo A Series\",\"marketname\":\"Lenovo A7\",\"internalname\":\"AK47\",\"sci_npi_pmd_program_name\":\"N/A\",\"design_type\":\"ODM-Fast Track\",\"odm\":\"Sprocomm\",\"modelname\":\"L19111\",\"blurname\":\"\",\"carrier\":\"N/A\",\"apccode\":\"N/A\",\"sadate\":\"\",\"dualsim\":\"\",\"isbarcode\":\"0\",\"launch_year\":\"FY20\",\"archive\":\"1\",\"design_type0\":\"ODM\",\"updatetime\":\"2020-06-05 17:40:494\"},{\"id\":\"458\",\"brand\":\"Lenovo\",\"productline\":\"PH\",\"productfamily\":\"Lenovo K Series\",\"marketname\":\"Lenovo K5\",\"internalname\":\"Austin2017\",\"sci_npi_pmd_program_name\":\"N/A\",\"design_type\":\"ODM-JDM\",\"odm\":\"Ontim\",\"modelname\":\"K350T\",\"blurname\":\"\",\"carrier\":\"N/A\",\"apccode\":\"N/A\",\"sadate\":\"\",\"dualsim\":\"\",\"isbarcode\":\"0\",\"launch_year\":\"FY18\",\"archive\":\"1\",\"design_type0\":\"ODM\",\"updatetime\":\"2020-06-05 17:40:494\"},{\"id\":\"457\",\"brand\":\"Lenovo\",\"productline\":\"PH\",\"productfamily\":\"Lenovo K Series\",\"marketname\":\"Lenovo K5 Play\",\"internalname\":\"K5 Young\",\"sci_npi_pmd_program_name\":\"N/A\",\"design_type\":\"ODM-Fast Track\",\"odm\":\"Huaqin\",\"modelname\":\"L38011\",\"blurname\":\"\",\"carrier\":\"N/A\",\"apccode\":\"N/A\",\"sadate\":\"\",\"dualsim\":\"\",\"isbarcode\":\"0\",\"launch_year\":\"FY18\",\"archive\":\"1\",\"design_type0\":\"ODM\",\"updatetime\":\"2020-06-05 17:40:493\"}]}";
                Message msg = new Message("qesTT" /* Topic */,
                        "product-addOrUpdate" /* Tag */,
                        "OrderId188" /* key */,
                        messageJson.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body,DEFAULT_CAHRSET:UTF-8*/
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();

    }
}

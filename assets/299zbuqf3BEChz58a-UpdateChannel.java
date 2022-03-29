package org.app.network;

import org.app.client.FabricClient;
import org.app.config.Config;
import org.app.user.UserContext;
import org.app.util.Util;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.UpdateChannelConfiguration;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;


public class UpdateChannel {

    public static void main(String[] args) {

        try {
            Util.cleanUp();
            UserContext calmAdmin = new UserContext();
            File pkFolder1 = new File(Config.CALM_USR_ADMIN_PK);
            File[] pkFiles1 = pkFolder1.listFiles();
            File certFolder1 = new File(Config.CALM_USR_ADMIN_CERT);
            File[] certFiles1 = certFolder1.listFiles();
            try {
                Enrollment enrollCalmAdmin = Util.getEnrollment(Config.CALM_USR_ADMIN_PK, Objects.requireNonNull(pkFiles1)[0].getName(),
                        Config.CALM_USR_ADMIN_CERT, Objects.requireNonNull(certFiles1)[0].getName());
                calmAdmin.setEnrollment(enrollCalmAdmin);
                calmAdmin.setMspId(Config.CALM_MSP);
                calmAdmin.setName(Config.ADMIN);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | CryptoException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            UserContext beamAdmin = new UserContext();
            File pkFolder2 = new File(Config.BEAM_USR_ADMIN_PK);
            File[] pkFiles2 = pkFolder2.listFiles();
            File certFolder2 = new File(Config.BEAM_USR_ADMIN_CERT);
            File[] certFiles2 = certFolder2.listFiles();
            try {
                Enrollment enrollBeamAdmin = Util.getEnrollment(Config.BEAM_USR_ADMIN_PK, Objects.requireNonNull(pkFiles2)[0].getName(),
                        Config.BEAM_USR_ADMIN_CERT, Objects.requireNonNull(certFiles2)[0].getName());
                beamAdmin.setEnrollment(enrollBeamAdmin);
                beamAdmin.setMspId(Config.BEAM_MSP);
                beamAdmin.setName(Config.ADMIN);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | CryptoException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            UserContext xiAdmin = new UserContext();
            File pkFolder3 = new File(Config.XI_USR_ADMIN_PK);
            File[] pkFiles3 = pkFolder3.listFiles();
            File certFolder3 = new File(Config.XI_USR_ADMIN_CERT);
            File[] certFiles3 = certFolder3.listFiles();
            try {
                Enrollment enrollXiAdmin = Util.getEnrollment(Config.XI_USR_ADMIN_PK, Objects.requireNonNull(pkFiles3)[0].getName(),
                        Config.XI_USR_ADMIN_CERT, Objects.requireNonNull(certFiles3)[0].getName());
                xiAdmin.setEnrollment(enrollXiAdmin);
                xiAdmin.setMspId(Config.XI_MSP);
                xiAdmin.setName(Config.ADMIN);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | CryptoException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            UserContext idpAdmin = new UserContext();
            File pkFolder4 = new File(Config.IDP_USR_ADMIN_PK);
            File[] pkFiles4 = pkFolder4.listFiles();
            File certFolder4 = new File(Config.IDP_USR_ADMIN_CERT);
            File[] certFiles4 = certFolder4.listFiles();
            try {
                Enrollment enrollIdpAdmin = Util.getEnrollment(Config.IDP_USR_ADMIN_PK, Objects.requireNonNull(pkFiles4)[0].getName(),
                        Config.IDP_USR_ADMIN_CERT, Objects.requireNonNull(certFiles4)[0].getName());
                idpAdmin.setEnrollment(enrollIdpAdmin);
                idpAdmin.setMspId(Config.IDP_MSP);
                idpAdmin.setName(Config.ADMIN);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | CryptoException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            FabricClient fabClient = new FabricClient(idpAdmin);

            UpdateChannelConfiguration updateChannelConfiguration = new UpdateChannelConfiguration(new File(args[0]));

            byte[][] updateChannelConfigurationSignatures = new byte[4][];

            try {
                updateChannelConfigurationSignatures[0] = fabClient.getInstance().getUpdateChannelConfigurationSignature(updateChannelConfiguration, idpAdmin);
                fabClient.getInstance().setUserContext(calmAdmin);
                updateChannelConfigurationSignatures[1] = fabClient.getInstance().getUpdateChannelConfigurationSignature(updateChannelConfiguration, calmAdmin);
                fabClient.getInstance().setUserContext(beamAdmin);
                updateChannelConfigurationSignatures[2] = fabClient.getInstance().getUpdateChannelConfigurationSignature(updateChannelConfiguration, beamAdmin);
                fabClient.getInstance().setUserContext(xiAdmin);
                updateChannelConfigurationSignatures[3] = fabClient.getInstance().getUpdateChannelConfigurationSignature(updateChannelConfiguration, xiAdmin);

                // ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(Config.CHANNEL_CONFIG_PATH));
                fabClient.getInstance().setUserContext(idpAdmin);

                Channel myChannel = fabClient.getInstance().newChannel(Config.CHANNEL_NAME);
                Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
                myChannel.addOrderer(orderer);

                try {
                    myChannel.initialize();
                    myChannel.updateChannelConfiguration(updateChannelConfiguration, orderer, updateChannelConfigurationSignatures);
                } catch (TransactionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (InvalidArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

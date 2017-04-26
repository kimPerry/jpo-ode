package gov.usdot.asn1.j2735;

import us.dot.its.jpo.ode.j2735.J2735;
import us.dot.its.jpo.ode.j2735.dsrc.DDay;
import us.dot.its.jpo.ode.j2735.dsrc.DFullTime;
import us.dot.its.jpo.ode.j2735.dsrc.DHour;
import us.dot.its.jpo.ode.j2735.dsrc.DMinute;
import us.dot.its.jpo.ode.j2735.dsrc.DMonth;
import us.dot.its.jpo.ode.j2735.dsrc.DYear;
import us.dot.its.jpo.ode.j2735.dsrc.Latitude;
import us.dot.its.jpo.ode.j2735.dsrc.Longitude;
import us.dot.its.jpo.ode.j2735.dsrc.Position3D;
import us.dot.its.jpo.ode.j2735.dsrc.Priority;
import us.dot.its.jpo.ode.j2735.dsrc.TemporaryID;
import us.dot.its.jpo.ode.j2735.semi.AdvisoryBroadcastType;
import us.dot.its.jpo.ode.j2735.semi.AdvisoryDetails;
import us.dot.its.jpo.ode.j2735.semi.AdvisorySituationData;
import us.dot.its.jpo.ode.j2735.semi.AdvisorySituationBundle;
import us.dot.its.jpo.ode.j2735.semi.AdvisorySituationBundle.AsdRecords;
import us.dot.its.jpo.ode.j2735.semi.BroadcastInstructions;
import us.dot.its.jpo.ode.j2735.semi.DataAcceptance;
import us.dot.its.jpo.ode.j2735.semi.DataConfirmation;
import us.dot.its.jpo.ode.j2735.semi.DataReceipt;
import us.dot.its.jpo.ode.j2735.semi.DistributionType;
import us.dot.its.jpo.ode.j2735.semi.DsrcInstructions;
import us.dot.its.jpo.ode.j2735.semi.GeoRegion;
import us.dot.its.jpo.ode.j2735.semi.GroupID;
import us.dot.its.jpo.ode.j2735.semi.Psid;
import us.dot.its.jpo.ode.j2735.semi.AdvisoryBroadcast;
import us.dot.its.jpo.ode.j2735.semi.AdvisorySituationDataDistribution;
import us.dot.its.jpo.ode.j2735.semi.Sha256Hash;
import us.dot.its.jpo.ode.j2735.semi.AdvisorySituationDataDistribution.AsdBundles;
import us.dot.its.jpo.ode.j2735.semi.DataRequest;
import us.dot.its.jpo.ode.j2735.semi.SemiDialogID;
import us.dot.its.jpo.ode.j2735.semi.SemiSequenceID;
import us.dot.its.jpo.ode.j2735.semi.TimeToLive;
import us.dot.its.jpo.ode.j2735.semi.TxChannel;
import us.dot.its.jpo.ode.j2735.semi.TxMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.oss.asn1.AbstractData;
import com.oss.asn1.Coder;
import com.oss.asn1.ControlTableNotFoundException;
import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;
import com.oss.asn1.InitializationException;
import com.oss.asn1.OctetString;

public class TravelerSampleMessageBuilder {
	
	final public static GroupID groupID = new GroupID(new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
	
	public static long recordCount = 3;
	public static long bundleCount = 3;
	
	static {
		try {
			J2735.initialize();
		} catch (ControlTableNotFoundException e) {
			e.printStackTrace();
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}

	public static AdvisorySituationData buildAdvisorySituationData() {
		AdvisorySituationData asd = new AdvisorySituationData();
		asd.setDialogID(SemiDialogID.advSitDataDep);
		asd.setSeqID(SemiSequenceID.data);
		asd.setGroupID(groupID);
		asd.setRequestID(new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array()));
		asd.setRecordID(new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array()));
		asd.setTimeToLive(TimeToLive.halfHour);
		
		GeoRegion serviceRegion = new GeoRegion();
		serviceRegion.setNwCorner(getPosition3D(43.0, -85.0));
		serviceRegion.setSeCorner(getPosition3D(41.0, -82.0));
		asd.setServiceRegion(serviceRegion);
		
		AdvisoryDetails asdmDetails = new AdvisoryDetails();
		asdmDetails.setAsdmID(new TemporaryID(ByteBuffer.allocate(4).putInt(5555).array()));
		asdmDetails.setAsdmType(AdvisoryBroadcastType.tim);
		
		DistributionType dt = new DistributionType(CVTypeHelper.DistributionType.RSU.arrayValue());
		asdmDetails.setDistType(dt);
		
		asdmDetails.setAdvisoryMessage(new OctetString("This is an advisory message.".getBytes()));
		asd.setAsdmDetails(asdmDetails);
		return asd;
	}
	
	public static DataRequest buildRsuAdvisorySituationDataRequest() {
		DataRequest request = new DataRequest();
		
		request.setDialogID(SemiDialogID.advSitDatDist);
		request.setSeqID(SemiSequenceID.dataReq);
		request.setGroupID(groupID);
		request.setRequestID(new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array()));
		
		GeoRegion serviceRegion = new GeoRegion();
		serviceRegion.setNwCorner(getPosition3D(48.374353, -131.643968));
		serviceRegion.setSeCorner(getPosition3D(24.156250, -72.347240));
		request.setServiceRegion(serviceRegion);
		
		DistributionType distType = new DistributionType(new byte[] { (byte) 0 } );
		request.setDistType(distType);
		
		return request;
	}
	
	public static AdvisorySituationDataDistribution buildRsuAdvisorySituationDataBundle() {
		AdvisorySituationDataDistribution bundles = new AdvisorySituationDataDistribution();

		bundles.setDialogID(SemiDialogID.advSitDatDist);
		bundles.setSeqID(SemiSequenceID.data);
		bundles.setGroupID(groupID);
		bundles.setRequestID(new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array()));
		
		bundles.setRecordCount(recordCount);
		bundles.setBundleCount(bundleCount);
		AsdRecords dataRecords = new AsdRecords(buildRSUAdvisoryBroadcastArray());
		AdvisorySituationBundle asdBundle = new AdvisorySituationBundle(1, new TemporaryID("1234".getBytes()), dataRecords);
		AsdBundles asdBundles = new AsdBundles(new AdvisorySituationBundle[] { asdBundle });
		bundles.setAsdBundles(asdBundles);
		return bundles;
	}
	
	public static AdvisoryBroadcast buildRSUAdvisoryBroadcast1() {
		AdvisoryBroadcast record1 = new AdvisoryBroadcast();
		
		Psid biPsid = new Psid("1234".getBytes());
		record1.setMessagePsid(biPsid);
		
		String msg1 = new String("Driving like there’s no tomorrow is likely to produce that result.");
		record1.setAdvisoryMessage(convertToOctet(msg1));
		record1.setBroadcastInst(generateBroadcastInstructions());
		
		return record1;
	}
		
	public static AdvisoryBroadcast buildRSUAdvisoryBroadcast2() {
		AdvisoryBroadcast record2 = new AdvisoryBroadcast();
		
		Psid biPsid = new Psid("1234".getBytes());
		record2.setMessagePsid(biPsid);
		
		String msg2 = new String("Click it or ticket.");
		record2.setAdvisoryMessage(convertToOctet(msg2));
		record2.setBroadcastInst(generateBroadcastInstructions());
		
		return record2;
	}

	public static AdvisoryBroadcast buildRSUAdvisoryBroadcast3() {
		AdvisoryBroadcast record3 = new AdvisoryBroadcast();
		
		Psid biPsid = new Psid("1234".getBytes());
		record3.setMessagePsid(biPsid);
		
		String msg3 = new String("Move over or get pulled over.");
		record3.setAdvisoryMessage(convertToOctet(msg3));
		record3.setBroadcastInst(generateBroadcastInstructions());

		return record3;	
	}
	
	public static AdvisoryBroadcast[] buildRSUAdvisoryBroadcastArray() {
		AdvisoryBroadcast[] recordsArray = {buildRSUAdvisoryBroadcast1(),buildRSUAdvisoryBroadcast2(),buildRSUAdvisoryBroadcast3()};
		
		return recordsArray;
	}
	
	public static DataConfirmation buildDataConfirmation() throws IOException {
		DataConfirmation confirmation = new DataConfirmation();
		confirmation.setDialogID(SemiDialogID.advSitDataDep);
		confirmation.setSeqID(SemiSequenceID.dataConf);
		confirmation.setGroupID(groupID);
		confirmation.setRequestID(new TemporaryID(J2735Util.mergeBytes(new byte[] { 0x20, 0x01, 0x3E, 0x16 } )));
		confirmation.setHash(new Sha256Hash(ByteBuffer.allocate(32).putInt(1).array()));
		return confirmation;
	}
	
	public static DataAcceptance buildDataAcceptance() throws IOException {
		DataAcceptance acceptance = new DataAcceptance();
		acceptance.setDialogID(SemiDialogID.advSitDataDep);
		acceptance.setSeqID(SemiSequenceID.accept);
		acceptance.setGroupID(groupID);
		acceptance.setRequestID(new TemporaryID(J2735Util.mergeBytes(new byte[] { 0x20, 0x01, 0x3E, 0x16 } )));
		return acceptance;
	}
	
	public static DataReceipt buildDataReceipt() throws IOException {
		DataReceipt receipt = new DataReceipt();
		receipt.setDialogID(SemiDialogID.advSitDataDep);
		receipt.setSeqID(SemiSequenceID.receipt);
		receipt.setGroupID(groupID);
		receipt.setRequestID(new TemporaryID(J2735Util.mergeBytes(new byte[] { 0x20, 0x01, 0x3E, 0x16 } )));
		return receipt;
	}
	
	private static BroadcastInstructions generateBroadcastInstructions(){
		DsrcInstructions di = new DsrcInstructions();
		di.setBiTxMode(TxMode.continuous);
		di.setBiTxChannel(TxChannel.chService);
		di.setBiTxInterval(10);
		
		BroadcastInstructions instr = new BroadcastInstructions();
		
		instr.setBiType(AdvisoryBroadcastType.spatAggregate);
		
		Priority biPriority = new Priority("1".getBytes());
		instr.setBiPriority(biPriority);
		
		//Not a realistic time set?
		DFullTime biDeliveryStart = new DFullTime(new DYear(2013), new DMonth(12), new DDay(9), new DHour(9), new DMinute(30));
		instr.setBiDeliveryStart(biDeliveryStart);
		
		DFullTime biDeliveryStop = new DFullTime(new DYear(2013), new DMonth(12), new DDay(9), new DHour(9), new DMinute(40));
		instr.setBiDeliveryStop(biDeliveryStop);
		
		instr.setBiSignature(false);
		instr.setBiEncryption(false);
		
		return instr;
	}

	// Note: Creates a new Coder for each call for thread safety, intended for testing only
	public static byte[] messageToEncodedBytes(AbstractData message) throws EncodeFailedException,
			EncodeNotSupportedException {
		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		Coder coder = J2735.getPERUnalignedCoder();
		coder.encode(message, sink);
		return sink.toByteArray();
	}

	public static Position3D getPosition3D(double lat, double lon) {
		Position3D pos = new Position3D(
			new Latitude(J2735Util.convertGeoCoordinateToInt(lat)), 
			new Longitude(J2735Util.convertGeoCoordinateToInt(lon)));
		return pos;
	}
	
	private static OctetString convertToOctet(String message){
		OctetString advisoryMessage = new OctetString(message.getBytes());
		return advisoryMessage;
	}
}

package gov.usdot.asn1.j2735;

import us.dot.its.jpo.ode.j2735.J2735;
import us.dot.its.jpo.ode.j2735.dsrc.DDateTime;
import us.dot.its.jpo.ode.j2735.dsrc.DDay;
import us.dot.its.jpo.ode.j2735.dsrc.DHour;
import us.dot.its.jpo.ode.j2735.dsrc.DMinute;
import us.dot.its.jpo.ode.j2735.dsrc.DMonth;
import us.dot.its.jpo.ode.j2735.dsrc.DOffset;
import us.dot.its.jpo.ode.j2735.dsrc.DSecond;
import us.dot.its.jpo.ode.j2735.dsrc.DYear;
import us.dot.its.jpo.ode.j2735.dsrc.DescriptiveName;
import us.dot.its.jpo.ode.j2735.dsrc.EnabledLaneList;
import us.dot.its.jpo.ode.j2735.dsrc.IntersectionID;
import us.dot.its.jpo.ode.j2735.dsrc.IntersectionReferenceID;
import us.dot.its.jpo.ode.j2735.dsrc.IntersectionState;
import us.dot.its.jpo.ode.j2735.dsrc.IntersectionStatusObject;
import us.dot.its.jpo.ode.j2735.dsrc.LaneID;
import us.dot.its.jpo.ode.j2735.dsrc.Latitude;
import us.dot.its.jpo.ode.j2735.dsrc.Longitude;
import us.dot.its.jpo.ode.j2735.dsrc.MapData;
import us.dot.its.jpo.ode.j2735.dsrc.MinuteOfTheYear;
import us.dot.its.jpo.ode.j2735.dsrc.MovementEvent;
import us.dot.its.jpo.ode.j2735.dsrc.MovementEventList;
import us.dot.its.jpo.ode.j2735.dsrc.MovementList;
import us.dot.its.jpo.ode.j2735.dsrc.MovementPhaseState;
import us.dot.its.jpo.ode.j2735.dsrc.MovementState;
import us.dot.its.jpo.ode.j2735.dsrc.MsgCount;
import us.dot.its.jpo.ode.j2735.dsrc.Position3D;
import us.dot.its.jpo.ode.j2735.dsrc.SignalGroupID;
import us.dot.its.jpo.ode.j2735.dsrc.TemporaryID;
import us.dot.its.jpo.ode.j2735.semi.DistributionType;
import us.dot.its.jpo.ode.j2735.semi.GeoRegion;
import us.dot.its.jpo.ode.j2735.semi.GroupID;
import us.dot.its.jpo.ode.j2735.semi.IntersectionBundle;
import us.dot.its.jpo.ode.j2735.semi.IntersectionRecord;
import us.dot.its.jpo.ode.j2735.semi.IntersectionSituationData;
import us.dot.its.jpo.ode.j2735.semi.IntersectionSituationDataAcceptance;
import us.dot.its.jpo.ode.j2735.semi.IntersectionSituationDataBundle;
import us.dot.its.jpo.ode.j2735.semi.DataRequest;
import us.dot.its.jpo.ode.j2735.semi.SemiDialogID;
import us.dot.its.jpo.ode.j2735.semi.SemiSequenceID;
import us.dot.its.jpo.ode.j2735.semi.SpatRecord;
import us.dot.its.jpo.ode.j2735.semi.TimeToLive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.oss.asn1.AbstractData;
import com.oss.asn1.Coder;
import com.oss.asn1.ControlTableNotFoundException;
import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;
import com.oss.asn1.INTEGER;
import com.oss.asn1.InitializationException;

public class IntersectionSitDataBuilder {
	
	final public static TemporaryID requestID = new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array());
	final public static GroupID groupID = new GroupID(new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
	
	static {
		try {
			J2735.initialize();
		} catch (ControlTableNotFoundException e) {
			e.printStackTrace();
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}
	
	public static IntersectionSituationData buildIntersectionSituationData() {
		IntersectionSituationData isd = new IntersectionSituationData();
		isd.setDialogID(SemiDialogID.intersectionSitDataDep);
		isd.setSeqID(SemiSequenceID.data);
		isd.setGroupID(groupID);
		isd.setRequestID(requestID);
		isd.setBundleNumber(new INTEGER(1));
		
		GeoRegion sr = new GeoRegion();
		sr.setNwCorner(getPosition3D(43.0, -85.0));
		sr.setSeCorner(getPosition3D(41.0, -82.0));
		isd.setServiceRegion(sr);
		isd.setTimeToLive(TimeToLive.halfHour);
		
		IntersectionRecord ir = new IntersectionRecord();
		MapData md1 = new MapData();
		md1.setMsgIssueRevision(new MsgCount(1));
		
		MovementEvent me = new MovementEvent();
		me.setEventState(MovementPhaseState.unavailable);
		
		MovementEventList mel = new MovementEventList(new MovementEvent[] { me });
		
		MovementState ms = new MovementState();
		ms.setMovementName(new DescriptiveName("Sample Movement 1") );
		ms.setSignalGroup(new SignalGroupID(1));
		ms.setState_time_speed(mel);

		for (int i = 0; i < 1; i++) {
			SpatRecord spr = new SpatRecord();
			spr.setTimestamp(
				new DDateTime(
				new DYear(2013), 
				new DMonth(12), 
				new DDay(9), 
				new DHour(9), 
				new DMinute(30),
				new DSecond(30),
				new DOffset(-300)));
			final IntersectionState intersectionState = new IntersectionState();
			intersectionState.setName(new DescriptiveName("Sample Intersection [" + i + "]"));
			intersectionState.setId(new IntersectionReferenceID(new IntersectionID(857)));
			intersectionState.setRevision(new MsgCount(0));
			intersectionState.setStatus(new IntersectionStatusObject(new byte [] { 0x01 }));
			intersectionState.setMoy(new MinuteOfTheYear(10000));
			intersectionState.setTimeStamp(new DSecond(2));
			intersectionState.setEnabledLanes(new EnabledLaneList(new LaneID[] { new LaneID(1), new LaneID(2) }));
			intersectionState.setStates(new MovementList(new MovementState[] { ms }));
			spr.setIntersections(intersectionState);
			ir.setMapData(md1);
			ir.setSpatData(spr);
		}
		
		isd.setIntersectionRecord(ir);
		return isd;
	}
	
	public static IntersectionSituationDataAcceptance buildIntersectionSituationDataAcceptance() throws IOException {
		return buildIntersectionSituationDataAcceptance(100);
	}
	
	public static IntersectionSituationDataAcceptance buildIntersectionSituationDataAcceptance(int recordsSent) throws IOException {
		IntersectionSituationDataAcceptance dataAcceptance = new IntersectionSituationDataAcceptance();
		dataAcceptance.setDialogID(SemiDialogID.intersectionSitDataDep);
		dataAcceptance.setSeqID(SemiSequenceID.accept);
		dataAcceptance.setGroupID(groupID);
		dataAcceptance.setRequestID(requestID);
		dataAcceptance.setRecordsSent(recordsSent);
		return dataAcceptance;
	}
	
	public static DataRequest buildIntersectionSituationDataRequest() {
		DataRequest isdr = new DataRequest();
		isdr.setDialogID(SemiDialogID.intersectionSitDataQuery);
		isdr.setSeqID(SemiSequenceID.dataReq);
		isdr.setGroupID(groupID);
		isdr.setRequestID(requestID);
		isdr.setTimeBound(new INTEGER(30));
		
		GeoRegion sr = new GeoRegion();
		sr.setNwCorner(getPosition3D(43.0, -85.0));
		sr.setSeCorner(getPosition3D(41.0, -82.0));
		isdr.setServiceRegion(sr);
		isdr.setTimeBound(15);
		
		DistributionType dt = new DistributionType(new byte[] { 0 } );
		isdr.setDistType(dt);
		return isdr;
	}
	
	public static IntersectionSituationDataBundle buildIntersectionSituationDataBundle() {
		IntersectionSituationDataBundle isdb = new IntersectionSituationDataBundle();
		isdb.setDialogID(SemiDialogID.intersectionSitDataQuery);
		isdb.setSeqID(SemiSequenceID.data);
		isdb.setGroupID(groupID);
		isdb.setRequestID(requestID);
		
		isdb.setRecordCount(4);
		isdb.setBundleCount(2);
		
		MapData md1 = new MapData();
		md1.setMsgIssueRevision(new MsgCount(1));
		
		MovementEvent me = new MovementEvent();
		me.setEventState(MovementPhaseState.unavailable);
		
		MovementEventList mel = new MovementEventList(new MovementEvent[] { me });
		
		MovementState ms = new MovementState();
		ms.setMovementName(new DescriptiveName("Sample Movement 1") );
		ms.setSignalGroup(new SignalGroupID(1));
		ms.setState_time_speed(mel);

		IntersectionBundle[] bundles = new IntersectionBundle[2];
		for (int i = 0; i < 2; i++) {
			IntersectionBundle ib = new IntersectionBundle();
			IntersectionRecord[] isdRecords = new IntersectionRecord[2];
			for (int j = 0; j < 2; j++) {
				IntersectionRecord ir = new IntersectionRecord();
				SpatRecord spr = new SpatRecord();
				spr.setTimestamp(
					new DDateTime(
					new DYear(2013), 
					new DMonth(12), 
					new DDay(9), 
					new DHour(9), 
					new DMinute(30),
					new DSecond(30),
					new DOffset(-300)));
				final IntersectionState intersectionState = new IntersectionState();
				intersectionState.setName(new DescriptiveName("Sample Intersection [" + j + "]"));
				intersectionState.setId(new IntersectionReferenceID(new IntersectionID(857)));
				intersectionState.setRevision(new MsgCount(0));
				intersectionState.setStatus(new IntersectionStatusObject(new byte [] { 0x01 }));
				intersectionState.setMoy(new MinuteOfTheYear(10000));
				intersectionState.setTimeStamp(new DSecond(2));
				intersectionState.setEnabledLanes(new EnabledLaneList(new LaneID[] { new LaneID(1), new LaneID(2) }));
				intersectionState.setStates(new MovementList(new MovementState[] { ms }));
				spr.setIntersections(intersectionState);
				ir.setMapData(md1);
				ir.setSpatData(spr);
				isdRecords[j] = ir;
			}
			ib.setIsdRecords(new IntersectionBundle.IsdRecords(isdRecords));
			ib.setBundleNumber(i+1);
			// ?? what should the tempID be ??
			ib.setBundleId(new TemporaryID(ByteBuffer.allocate(4).putInt(1001).array()));
			bundles[i] = ib;
		}
		
		isdb.setIsdBundles(new IntersectionSituationDataBundle.IsdBundles(bundles));
		return isdb;
	}
	
	public static byte [] messageToEncodedBytes(AbstractData message) throws EncodeFailedException, EncodeNotSupportedException {
		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		Coder coder = J2735.getPERUnalignedCoder();
		coder.encode(message, sink);
		return sink.toByteArray();
	}
	
	private static Position3D getPosition3D(double lat, double lon) {
		Position3D pos = new Position3D(
			new Latitude(J2735Util.convertGeoCoordinateToInt(lat)), 
			new Longitude(J2735Util.convertGeoCoordinateToInt(lon)));
		return pos;
	}
	
}
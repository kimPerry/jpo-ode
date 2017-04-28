package us.dot.its.jpo.ode.vsdm;

import static org.junit.Assert.fail;

import org.apache.tomcat.util.buf.HexUtils;
import org.junit.Test;

import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;
import com.oss.asn1.PERUnalignedCoder;

import us.dot.its.jpo.ode.j2735.J2735;
import us.dot.its.jpo.ode.j2735.dsrc.Acceleration;
import us.dot.its.jpo.ode.j2735.dsrc.AccelerationSet4Way;
import us.dot.its.jpo.ode.j2735.dsrc.AntiLockBrakeStatus;
import us.dot.its.jpo.ode.j2735.dsrc.AuxiliaryBrakeStatus;
import us.dot.its.jpo.ode.j2735.dsrc.BrakeAppliedStatus;
import us.dot.its.jpo.ode.j2735.dsrc.BrakeBoostApplied;
import us.dot.its.jpo.ode.j2735.dsrc.BrakeSystemStatus;
import us.dot.its.jpo.ode.j2735.dsrc.DDateTime;
import us.dot.its.jpo.ode.j2735.dsrc.DDay;
import us.dot.its.jpo.ode.j2735.dsrc.DHour;
import us.dot.its.jpo.ode.j2735.dsrc.DMinute;
import us.dot.its.jpo.ode.j2735.dsrc.DMonth;
import us.dot.its.jpo.ode.j2735.dsrc.DOffset;
import us.dot.its.jpo.ode.j2735.dsrc.DSecond;
import us.dot.its.jpo.ode.j2735.dsrc.DYear;
import us.dot.its.jpo.ode.j2735.dsrc.Heading;
import us.dot.its.jpo.ode.j2735.dsrc.Latitude;
import us.dot.its.jpo.ode.j2735.dsrc.Longitude;
import us.dot.its.jpo.ode.j2735.dsrc.MsgCRC;
import us.dot.its.jpo.ode.j2735.dsrc.Position3D;
import us.dot.its.jpo.ode.j2735.dsrc.StabilityControlStatus;
import us.dot.its.jpo.ode.j2735.dsrc.SteeringWheelAngle;
import us.dot.its.jpo.ode.j2735.dsrc.TemporaryID;
import us.dot.its.jpo.ode.j2735.dsrc.TractionControlStatus;
import us.dot.its.jpo.ode.j2735.dsrc.TransmissionAndSpeed;
import us.dot.its.jpo.ode.j2735.dsrc.TransmissionState;
import us.dot.its.jpo.ode.j2735.dsrc.VehicleLength;
import us.dot.its.jpo.ode.j2735.dsrc.VehicleSize;
import us.dot.its.jpo.ode.j2735.dsrc.VehicleWidth;
import us.dot.its.jpo.ode.j2735.dsrc.Velocity;
import us.dot.its.jpo.ode.j2735.dsrc.VerticalAcceleration;
import us.dot.its.jpo.ode.j2735.dsrc.YawRate;
import us.dot.its.jpo.ode.j2735.semi.FundamentalSituationalStatus;
import us.dot.its.jpo.ode.j2735.semi.GroupID;
import us.dot.its.jpo.ode.j2735.semi.SemiDialogID;
import us.dot.its.jpo.ode.j2735.semi.SemiSequenceID;
import us.dot.its.jpo.ode.j2735.semi.VehSitDataMessage;
import us.dot.its.jpo.ode.j2735.semi.VehSitDataMessage.Bundle;
import us.dot.its.jpo.ode.j2735.semi.VehSitRecord;
import us.dot.its.jpo.ode.j2735.semi.VsmType;

public class VsdToBsmConverterTest {

	@Test
	public void createVSDAndEncode() {
		VehSitRecord vsr = new VehSitRecord();

		vsr.setTempID(new TemporaryID(new byte[] {1,2,3,4 }));

		vsr.setTime(new DDateTime(new DYear(2017), new DMonth(4), new DDay(28), new DHour(12), new DMinute(42),
				new DSecond(35), new DOffset(0)));

		vsr.setPos(new Position3D(new Latitude(85), new Longitude(85)));

		vsr.setFundamental(new FundamentalSituationalStatus(
				new TransmissionAndSpeed(new TransmissionState(0), new Velocity(60)), new Heading(55),
				new SteeringWheelAngle(55),
				new AccelerationSet4Way(new Acceleration(1), new Acceleration(2), new VerticalAcceleration(3),
						new YawRate(4)),
				new BrakeSystemStatus(new BrakeAppliedStatus(new byte[] {8}), new TractionControlStatus(0),
						new AntiLockBrakeStatus(2), new StabilityControlStatus(3), new BrakeBoostApplied(1),
						new AuxiliaryBrakeStatus(2)),
				new VehicleSize(new VehicleWidth(5), new VehicleLength(6))));

		VehSitDataMessage vsdm = new VehSitDataMessage();

		vsdm.setBundle(new Bundle(new VehSitRecord[] { vsr }));

		vsdm.setDialogID(new SemiDialogID(154));
		vsdm.setSeqID(new SemiSequenceID(5));
		vsdm.setRequestID(new TemporaryID(new byte[] {1,2,3,4 }));
		vsdm.setType(new VsmType(new byte[] { 20 }));
		vsdm.setCrc(new MsgCRC(new byte[] {1,2}));
		vsdm.setGroupID(new GroupID(new byte[] {1,2,3,4}));

		PERUnalignedCoder coder = J2735.getPERUnalignedCoder();

		String hexEncodedVsdm = "";

		try {
			hexEncodedVsdm = HexUtils.toHexString(coder.encode(vsdm).array());
		} catch (EncodeFailedException | EncodeNotSupportedException e) {
			fail("Exception: " + e);
		}

		System.out.println(hexEncodedVsdm);

	}
}

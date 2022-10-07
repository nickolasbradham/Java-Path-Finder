package nbradham.pathing;

import java.awt.Point;
import java.util.ArrayList;

final class Human {

	private final ArrayList<Point> poss = new ArrayList<>();

	void addKeyPos(short x, short y, short t) {
		if (poss.size() < t) {
			Point last = poss.get(poss.size() - 1);
		}
	}
}
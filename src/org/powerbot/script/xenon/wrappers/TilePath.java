package org.powerbot.script.xenon.wrappers;

import java.util.Arrays;
import java.util.EnumSet;

import org.powerbot.script.xenon.Calculations;
import org.powerbot.script.xenon.Players;
import org.powerbot.script.xenon.Walking;
import org.powerbot.script.xenon.util.Random;

public class TilePath extends Path {
	protected Tile[] tiles;
	protected Tile[] orig;
	private boolean end;

	public TilePath(final Tile[] tiles) {
		orig = tiles;
		this.tiles = Arrays.copyOf(tiles, tiles.length);
	}

	@Override
	public boolean traverse(final EnumSet<TraversalOption> options) {
		final Player local = Players.getLocal();
		final Tile next = getNext();
		if (next == null || local == null) return false;
		final Tile dest = Walking.getDestination();
		if (next.equals(getEnd())) {
			if (Calculations.distanceTo(next) <= 1) return false;
			if (end && (local.isMoving() || (dest != null && dest.equals(next)))) return false;
			end = true;
		} else end = false;
		if (options != null) {
			if (options.contains(TraversalOption.HANDLE_RUN) && !Walking.isRunEnabled() && Walking.getEnergy() > Random.nextInt(45, 60)) {
				Walking.toggleRun(true);
			}
			if (options.contains(TraversalOption.SPACE_ACTIONS) && dest != null && local.isMoving() && Calculations.distance(next, dest) < 3d) {
				if (Calculations.distanceTo(dest) > Random.nextDouble(4d, 6d)) return true;
			}
		}
		return Walking.stepTowards(next);
	}

	@Override
	public boolean isValid() {
		return tiles.length > 0 && getNext() != null && Calculations.distanceTo(getEnd()) > Math.sqrt(2);
	}

	@Override
	public Tile getNext() {
		final Tile dest = Walking.getDestination();
		for (int i = tiles.length - 1; i >= 0; --i) {
			if (!tiles[i].isOnMap()) continue;
			if (dest == null || Calculations.distance(dest, tiles[i - 1]) < 3) return tiles[i];
		}
		return null;
	}

	@Override
	public Tile getStart() {
		return tiles[0];
	}

	@Override
	public Tile getEnd() {
		return tiles[tiles.length - 1];
	}

	public TilePath randomize(final int maxX, final int maxY) {
		for (int i = 0; i < tiles.length; ++i) {
			tiles[i] = orig[i].derive(Random.nextInt(-maxX, maxX + 1), Random.nextInt(-maxY, maxY + 1));
		}
		return this;
	}

	public TilePath reverse() {
		Tile[] reversed = new Tile[tiles.length];
		for (int i = 0; i < orig.length; ++i) {
			reversed[i] = orig[tiles.length - 1 - i];
		}
		orig = reversed;
		reversed = new Tile[tiles.length];
		for (int i = 0; i < tiles.length; ++i) {
			reversed[i] = tiles[tiles.length - 1 - i];
		}
		tiles = reversed;
		return this;
	}

	public Tile[] toArray() {
		final Tile[] a = new Tile[tiles.length];
		System.arraycopy(tiles, 0, a, 0, tiles.length);
		return a;
	}
}
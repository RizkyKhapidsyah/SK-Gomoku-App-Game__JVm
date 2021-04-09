package gameml.gomoku;

// AI with MINMAX Algorithm. 
public class GomokuAI {

	private final int boardSize;
	private final int userSq = 1;
	private final int machSq = -1;

	private final int MyWinningMove = 99999999;
	private final int winningMove = 9999999;
	private final int openFour = 8888888;
	private final int twoThrees = 777777;

	private int[][] f;
	private int[][] s;
	private int[][] q;

	public int ResultCol;
	public int ResultRow;

	public GomokuAI(int GridSize) {
		boardSize = GridSize - 1;
	}

	private int hasNeighbors(int i, int j) {
		if (j > 0 && f[i][j - 1] != 0)
			return 1;
		if (j + 1 < boardSize && f[i][j + 1] != 0)
			return 1;
		if (i > 0) {
			if (f[i - 1][j] != 0)
				return 1;
			if (j > 0 && f[i - 1][j - 1] != 0)
				return 1;
			if (j + 1 < boardSize && f[i - 1][j + 1] != 0)
				return 1;
		}
		if (i + 1 < boardSize) {
			if (f[i + 1][j] != 0)
				return 1;
			if (j > 0 && f[i + 1][j - 1] != 0)
				return 1;
			if (j + 1 < boardSize && f[i + 1][j + 1] != 0)
				return 1;
		}
		return 0;
	}

	private int winningPos(int i, int j, int mySq) {
		int test3 = 0;

		int L = 1;
		int m = 1;
		while (j + m < boardSize && f[i][j + m] == mySq) {
			L++;
			m++;
		}

		int m1 = m;
		m = 1;
		while (j - m >= 0 && f[i][j - m] == mySq) {
			L++;
			m++;
		}
		int m2 = m;

		if (L > 4) {
			return winningMove;
		}

		boolean side1 = (j + m1 < boardSize && f[i][j + m1] == 0);
		boolean side2 = (j - m2 >= 0 && f[i][j - m2] == 0);

		if (L == 4 && (side1 || side2))
			test3++;

		if (side1 && side2) {
			if (L == 4)
				return openFour;
			if (L == 3)
				test3++;
		}

		L = 1;
		m = 1;
		while (i + m < boardSize && f[i + m][j] == mySq) {
			L++;
			m++;
		}
		m1 = m;
		m = 1;

		while (i - m >= 0 && f[i - m][j] == mySq) {
			L++;
			m++;
		}
		m2 = m;

		if (L > 4) {
			return winningMove;
		}

		side1 = (i + m1 < boardSize && f[i + m1][j] == 0);
		side2 = (i - m2 >= 0 && f[i - m2][j] == 0);

		if (L == 4 && (side1 || side2))
			test3++;
		if (side1 && side2) {
			if (L == 4)
				return openFour;
			if (L == 3)
				test3++;
		}
		if (test3 == 2)
			return twoThrees;

		L = 1;
		m = 1;
		while (i + m < boardSize && j + m < boardSize
				&& f[i + m][j + m] == mySq) {
			L++;
			m++;
		}
		m1 = m;

		m = 1;
		while (i - m >= 0 && j - m >= 0 && f[i - m][j - m] == mySq) {
			L++;
			m++;
		}
		m2 = m;

		if (L > 4) {
			return winningMove;
		}

		side1 = (i + m1 < boardSize && j + m1 < boardSize && f[i + m1][j + m1] == 0);
		side2 = (i - m2 >= 0 && j - m2 >= 0 && f[i - m2][j - m2] == 0);

		if (L == 4 && (side1 || side2))
			test3++;
		if (side1 && side2) {
			if (L == 4)
				return openFour;
			if (L == 3)
				test3++;
		}

		if (test3 == 2)
			return twoThrees;

		L = 1;
		m = 1;
		while (i + m < boardSize && j - m >= 0 && f[i + m][j - m] == mySq) {
			L++;
			m++;
		}
		m1 = m;
		m = 1;

		while (i - m >= 0 && j + m < boardSize && f[i - m][j + m] == mySq) {
			L++;
			m++;
		}
		m2 = m;

		if (L > 4) {
			return winningMove;
		}

		side1 = (i + m1 < boardSize && j - m1 >= 0 && f[i + m1][j - m1] == 0);
		side2 = (i - m2 >= 0 && j + m2 < boardSize && f[i - m2][j + m2] == 0);
		if (L == 4 && (side1 || side2))
			test3++;
		if (side1 && side2) {
			if (L == 4)
				return openFour;
			if (L == 3)
				test3++;
		}
		if (test3 == 2)
			return twoThrees;
		return -1;
	}

	private int evaluatePos(int[][] a, int mySq) {

		int maxA = -1;

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				// Compute "value" a[i][j] of the (i,j) move

				if (f[i][j] != 0) {

					a[i][j] = -1;

					continue;
				}
				if (hasNeighbors(i, j) == 0) {
					a[i][j] = -1;
					continue;
				}

				int wp = winningPos(i, j, mySq);

				if (wp == winningMove) {
					a[i][j] = winningMove;
					return winningMove;
				}

				if (wp >= twoThrees) {
					a[i][j] = wp;
					if (maxA < wp)
						maxA = wp;
					continue;
				}

				int minM = i - 4;
				if (minM < 0)
					minM = 0;

				int minN = j - 4;
				if (minN < 0)
					minN = 0;

				int maxM = i + 5;
				if (maxM > boardSize)
					maxM = boardSize;
				int maxN = j + 5;
				if (maxN > boardSize)
					maxN = boardSize;

				double w[] = { 0, 20, 17, 15.4, 14, 10 };

				int[] dirA;
				dirA = new int[5];

				int[] nPos;
				nPos = new int[5];
				nPos[1] = 1;

				int A1 = 0;
				int m = 1;

				while (j + m < maxN && f[i][j + m] != -mySq) {
					nPos[1]++;
					A1 += w[m] * f[i][j + m];
					m++;
				}

				if (j + m >= boardSize || f[i][j + m] == -mySq)
					A1 -= (f[i][j + m - 1] == mySq) ? (w[5] * mySq) : 0;
				m = 1;
				while (j - m >= minN && f[i][j - m] != -mySq) {
					nPos[1]++;
					A1 += w[m] * f[i][j - m];
					m++;
				}
				if (j - m < 0 || f[i][j - m] == -mySq)
					A1 -= (f[i][j - m + 1] == mySq) ? (w[5] * mySq) : 0;

				nPos[2] = 1;
				int A2 = 0;
				m = 1;
				while (i + m < maxM && f[i + m][j] != -mySq) {
					nPos[2]++;
					A2 += w[m] * f[i + m][j];
					m++;
				}

				if (i + m >= boardSize || f[i + m][j] == -mySq)
					A2 -= (f[i + m - 1][j] == mySq) ? (w[5] * mySq) : 0;

				m = 1;
				while (i - m >= minM && f[i - m][j] != -mySq) {
					nPos[2]++;
					A2 += w[m] * f[i - m][j];
					m++;
				}

				if (i - m < 0 || f[i - m][j] == -mySq)
					A2 -= (f[i - m + 1][j] == mySq) ? (w[5] * mySq) : 0;

				nPos[3] = 1;
				int A3 = 0;

				m = 1;

				while (i + m < maxM && j + m < maxN && f[i + m][j + m] != -mySq) {
					nPos[3]++;
					A3 += w[m] * f[i + m][j + m];
					m++;
				}

				if (i + m >= boardSize || j + m >= boardSize
						|| f[i + m][j + m] == -mySq)
					A3 -= (f[i + m - 1][j + m - 1] == mySq) ? (w[5] * mySq) : 0;

				m = 1;
				while (i - m >= minM && j - m >= minN
						&& f[i - m][j - m] != -mySq) {
					nPos[3]++;
					A3 += w[m] * f[i - m][j - m];
					m++;
				}
				if (i - m < 0 || j - m < 0 || f[i - m][j - m] == -mySq)
					A3 -= (f[i - m + 1][j - m + 1] == mySq) ? (w[5] * mySq) : 0;

				nPos[4] = 1;
				int A4 = 0;
				m = 1;
				while (i + m < maxM && j - m >= minN
						&& f[i + m][j - m] != -mySq) {
					nPos[4]++;
					A4 += w[m] * f[i + m][j - m];
					m++;
				}

				if (i + m >= boardSize || j - m < 0 || f[i + m][j - m] == -mySq)
					A4 -= (f[i + m - 1][j - m + 1] == mySq) ? (w[5] * mySq) : 0;
				m = 1;
				while (i - m >= minM && j + m < maxN
						&& f[i - m][j + m] != -mySq) {
					nPos[4]++;
					A4 += w[m] * f[i - m][j + m];
					m++;
				}

				if (i - m < 0 || j + m >= boardSize || f[i - m][j + m] == -mySq)
					A4 -= (f[i - m + 1][j + m - 1] == mySq) ? (w[5] * mySq) : 0;

				dirA[1] = (nPos[1] > 4) ? A1 * A1 : 0;
				dirA[2] = (nPos[2] > 4) ? A2 * A2 : 0;
				dirA[3] = (nPos[3] > 4) ? A3 * A3 : 0;
				dirA[4] = (nPos[4] > 4) ? A4 * A4 : 0;

				A1 = 0;
				A2 = 0;
				for (int k = 1; k < 5; k++) {
					if (dirA[k] >= A1) {
						A2 = A1;
						A1 = dirA[k];
					}
				}

				int thisA = A1 + A2;

				a[i][j] = thisA;

				if (thisA > maxA) {
					maxA = thisA;
				}
			}
		}
		return maxA;
	}

	public void machineMove(int iUser, int jUser, int[][] GridArray) {

		int iMach = 0;
		int jMach = 0;

		s = new int[boardSize][boardSize];
		f = new int[boardSize][boardSize];
		q = new int[boardSize][boardSize];

		for (int k = 0; k < boardSize; k++) {
			for (int k2 = 0; k2 < boardSize; k2++) {
				s[k][k2] = f[k][k2] = q[k][k2] = GridArray[k][k2];
			}
		}

		int maxS = evaluatePos(s, userSq);
		int maxQ = evaluatePos(q, machSq);

		if (maxQ >= maxS) {
			maxS = -1;
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {

					if (q[i][j] == maxQ && s[i][j] > maxS) {
						maxS = s[i][j];
						iMach = i;
						jMach = j;
					}
				}
			}
		} else {
			maxQ = -1;

			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					if (s[i][j] == maxS && q[i][j] > maxQ) {
						maxQ = q[i][j];
						iMach = i;
						jMach = j;
					}
				}
			}
		}

		f[iMach][jMach] = machSq;
		ResultCol = iMach;
		ResultRow = jMach;

	}

}

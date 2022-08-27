import sqlite3
from daos import _Vaccines, _Clinics, _Logistics, _Suppliers
import atexit

class Repository(object):
    def __init__(self):
        self._conn = sqlite3.connect('database.db')
        self.vaccines = _Vaccines(self._conn)
        self.suppliers = _Suppliers(self._conn)
        self.clinics = _Clinics(self._conn)
        self.logistics = _Logistics(self._conn)

    def _close(self):
        self._conn.commit()
        self._conn.close()

    def create_tables(self):
        self._conn.executescript("""
            CREATE TABLE vaccines (
                id INT PRIMARY KEY,
                date DATE NOT NULL,
                supplier INT REFERENCES suppliers(id),
                quantity INT NOT NULL
            );

            CREATE TABLE suppliers (
                id INT PRIMARY KEY,
                name TEXT NOT NULL,
                logistic INT REFERENCES logistics(id)
            );

            CREATE TABLE clinics (
                id INT PRIMARY KEY,
                location TEXT NOT NULL,
                demand INT NOT NULL,
                logistic INT REFERENCES logistics(id)

            );
            
            CREATE TABLE logistics (
                id INT PRIMARY KEY,
                name TEXT NOT NULL,
                count_sent INT NOT NULL,
                count_received INT NOT NULL
            );
        """)

    def output_line(self):
        cursor = self._conn.cursor()
        cursor.execute("""
            SELECT SUM (quantity) FROM vaccines
        """)
        total_vaccines = cursor.fetchone()[0]
        cursor.execute("""
            SELECT SUM (demand) FROM clinics
        """)
        total_demand = cursor.fetchone()[0]
        cursor.execute("""
            SELECT SUM (count_received), SUM (count_sent) FROM logistics
        """)
        total_received, total_sent = cursor.fetchone()
        return [total_vaccines, total_demand, total_received, total_sent]

repo = Repository()
atexit.register(repo._close)
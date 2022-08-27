class _Vaccines:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, vaccine):
        self._conn.execute("""
            INSERT INTO vaccines (id, date, supplier, quantity) VALUES (?, ?, ?, ?)
        """, [vaccine.id, vaccine.date, vaccine.supplier, vaccine.quantity])
        self._conn.commit()

    def update_quantity(self, amount):
        cursor = self._conn.cursor()
        cursor.execute("""
            SELECT id,quantity FROM vaccines LIMIT 1
        """)
        vaccine = cursor.fetchone()
        amount_to_remove = min(amount, vaccine[1])
        if amount_to_remove != amount:
            self._conn.execute("""
                DELETE FROM vaccines WHERE id = (?)
            """, [vaccine[0]])

        else:
            self._conn.execute("""
                UPDATE vaccines SET quantity = (?) WHERE id=(?)
            """, [vaccine[1] - amount_to_remove, vaccine[0]])
        self._conn.commit()
        return amount_to_remove

class _Suppliers:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, supplier):
        self._conn.execute("""
                INSERT INTO suppliers (id, name, logistic) VALUES (?, ?, ?)
            """, [supplier.id, supplier.name, supplier.logistic])
        self._conn.commit()

    def update_logistic(self, supplier_name, amount):
        cursor = self._conn.cursor()
        cursor.execute("""
                SELECT logistic FROM suppliers WHERE name = ?
        """, [supplier_name])
        logistic = cursor.fetchone()
        cursor.execute("""
                SELECT count_received FROM logistics WHERE id = ?
        """, logistic)
        update_count_received = cursor.fetchone()[0]+amount
        self._conn.execute("""
                UPDATE logistics SET count_received=(?) WHERE id=(?)
        """, [update_count_received, logistic[0]])
        self._conn.commit()

    def get_id(self, supplier_name):
        cursor = self._conn.cursor()
        cursor.execute("""
                SELECT id FROM suppliers WHERE name = ?
        """, [supplier_name])
        return cursor.fetchone()[0]

class _Clinics:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, clinic):
        self._conn.execute("""
                INSERT INTO clinics (id, location, demand, logistic) VALUES (?, ?, ?, ?)
            """, [clinic.id, clinic.location, clinic.demand, clinic.logistic])
        self._conn.commit()


    def update_logistic(self, clinic_location, amount):
        cursor = self._conn.cursor()
        cursor.execute("""
                SELECT demand ,logistic FROM clinics WHERE location = ?
        """, [clinic_location])
        current_demand, logistic = cursor.fetchone()
        self._conn.execute("""
                UPDATE clinics SET demand = (?) WHERE location = (?)
        """, [current_demand-amount ,clinic_location])

        cursor.execute("""
                SELECT count_sent FROM logistics WHERE id = ?
        """, [logistic])
        update_count_sent = cursor.fetchone()[0]+ amount
        self._conn.execute("""
                UPDATE logistics SET count_sent=(?) WHERE id=(?)
        """, [update_count_sent, logistic])
        self._conn.commit()


class _Logistics:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, logistic):
        self._conn.execute("""
            INSERT INTO logistics (id, name, count_sent, count_received) VALUES (?, ?, ?, ?)
        """, [logistic.id, logistic.name, logistic.count_sent, logistic.count_received])
        self._conn.commit()

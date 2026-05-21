import { useEffect, useState } from "react";

import Loader from "../../../shared/Loader";

import InventoryTable from "../components/InventoryTable";

import {
  getInventoryByFuelType,
} from "../services/inventoryService";


const Inventory = () => {

  const [inventory, setInventory] = useState([]);

  const [loading, setLoading] = useState(true);


  // ==============================
  // Fetch Inventory
  // ==============================

  const fetchInventory = async () => {

    try {

      setLoading(true);


      // Parallel API Calls
      const [
        petrolStock,
        dieselStock,
      ] = await Promise.all([

        getInventoryByFuelType("PETROL"),

        getInventoryByFuelType("DIESEL"),
      ]);


      // Combine Response
      setInventory([
        petrolStock,
        dieselStock,
      ]);

    } catch (error) {

      console.log(error);

    } finally {

      setLoading(false);
    }
  };


  // ==============================
  // Initial Fetch
  // ==============================

  useEffect(() => {

    const loadInventory = async () => {

      await fetchInventory();
    };

    loadInventory();

  }, []);


  if (loading) {

    return <Loader />;
  }


  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Inventory
      </h1>

      <InventoryTable inventory={inventory} />

    </div>
  );
};

export default Inventory;
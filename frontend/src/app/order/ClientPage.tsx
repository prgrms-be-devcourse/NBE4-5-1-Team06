"use client";
import axios from "axios";
import Link from "next/link";
import { useState } from "react";

type CoffeeOrder = {
  id: number;
  coffee: {
    id: number;
    name: string;
    price: number;
  };
  quantity: number;
};

type Order = {
  id: number;
  email: string;
  orderTime: string;
  modifyTime: string;
  status: boolean;
  address: string;
  totalPrice: number;
  orderCoffees: CoffeeOrder[];
};

axios.defaults.baseURL = "http://localhost:8080";

export default function ClientPage() {
  const [email, setEmail] = useState("");
  const [orders, setOrders] = useState<Order[]>([]);
  const [modifiedCoffees, setModifiedCoffees] = useState<{
    [orderId: number]: { [coffeeId: number]: number };
  }>({});

  const getData = async () => {
    try {
      const response = await axios.get(`/api/order/${email}`);
      console.log(response.data);
      setOrders(response.data.orders);
    } catch (error) {
      alert("해당 이메일의 주문은 존재하지 않습니다.");
    }
  };

  // 각 주문의 수정 상태 관리
  const [editingOrder, setEditingOrder] = useState<number | null>(null); // 수정 상태를 추적할 변수 (orderId)

  const toggleEditing = (orderId: number) => {
    if (editingOrder === orderId) {
      setEditingOrder(null); // 수정 중인 주문을 다시 완료 상태로 변경
      // 수정 완료 시 서버에 업데이트 요청
      updateOrder(orderId); // 업데이트 API 호출
    } else {
      setEditingOrder(orderId); // 해당 주문을 수정 모드로 변경
    }
  };

  // 총 가격 업데이트
  const calculateTotalPrice = (order: Order) => {
    return order.orderCoffees.reduce(
      (total, coffeeOrder) =>
        total + coffeeOrder.coffee.price * coffeeOrder.quantity,
      0
    );
  };

  // 수량 증가
  const handleIncrease = (orderId: number, coffeeId: number) => {
    setOrders((prevOrders) =>
      prevOrders.map((order) =>
        order.id === orderId
          ? {
              ...order,
              orderCoffees: order.orderCoffees.map((coffeeOrder) =>
                coffeeOrder.id === coffeeId
                  ? { ...coffeeOrder, quantity: coffeeOrder.quantity + 1 }
                  : coffeeOrder
              ),
            }
          : order
      )
    );

    // 변경된 커피 ID 및 수량 저장
    setModifiedCoffees((prev) => ({
      ...prev,
      [orderId]: {
        ...(prev[orderId] || {}),
        [coffeeId]: (prev[orderId]?.[coffeeId] || 0) + 1, // 수량 증가
      },
    }));
  };

  // 수량 감소
  const handleDecrease = (orderId: number, coffeeId: number) => {
    setOrders((prevOrders) =>
      prevOrders.map((order) =>
        order.id === orderId
          ? {
              ...order,
              orderCoffees: order.orderCoffees.map((coffeeOrder) =>
                coffeeOrder.id === coffeeId && coffeeOrder.quantity > 1
                  ? { ...coffeeOrder, quantity: coffeeOrder.quantity - 1 }
                  : coffeeOrder
              ),
            }
          : order
      )
    );

    // 변경된 커피 ID 및 수량 저장
    setModifiedCoffees((prev) => ({
      ...prev,
      [orderId]: {
        ...(prev[orderId] || {}),
        [coffeeId]: (prev[orderId]?.[coffeeId] || 0) - 1, // 수량 감소
      },
    }));
  };

  // 서버에 모든 변경된 커피 업데이트
  const updateOrder = async (orderId: number) => {
    if (window.confirm("정말 커피를 수정하시겠습니까?")) {
      try {
        const orderToUpdate = orders.find((order) => order.id === orderId);
        if (!orderToUpdate) return;

        const modifiedCoffeeData = modifiedCoffees[orderId] || {};

        // 변경된 커피 리스트 생성
        const updatedCoffees = orderToUpdate.orderCoffees
          .filter(
            (coffeeOrder) => modifiedCoffeeData[coffeeOrder.id] !== undefined
          )
          .map((coffeeOrder) => ({
            coffeeId: coffeeOrder.coffee.id,
            quantity: coffeeOrder.quantity,
          }));

        if (updatedCoffees.length === 0) {
          alert("변경된 내용이 없습니다.");
          return;
        }

        const updatedOrderData = {
          email: orderToUpdate.email,
          coffees: updatedCoffees,
        };

        const response = await axios.patch(
          `/api/order/${orderId}`,
          updatedOrderData
        );
        console.log("Updated order:", response.data);

        // 상태 업데이트
        setOrders((prevOrders) =>
          prevOrders.map((order) =>
            order.id === orderId ? { ...order, ...response.data } : order
          )
        );

        // 수정 완료 후 해당 주문의 변경 내역 초기화
        setModifiedCoffees((prev) => {
          const updated = { ...prev };
          delete updated[orderId];
          return updated;
        });

        alert("수정이 완료되었습니다.");
      } catch (error) {
        alert("수정 실패");
      }
    }
  };

  const handleDelete = async (orderId: number) => {
    if (window.confirm("정말 이 주문을 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/api/order/${orderId}`);
        setOrders(orders.filter((order) => order.id !== orderId)); // 삭제 후 로컬 상태 업데이트
        alert("주문이 삭제되었습니다.");
      } catch (error) {
        alert("삭제 중 오류가 발생했습니다.");
      }
    }
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen">
      <Link href={"/"}>
        <div className="text-4xl font-bold">Grids & Circles</div>
      </Link>
      <div className="container flex flex-col px-8 py-5 text-xl font-bold mt-10 overflow-y-auto">
        <div className="flex flex-row justify-center items-center">
          email : &nbsp;
          <input
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="border border-gray-300 rounded-md px-4 py-2"
          />
          <button
            onClick={getData}
            className="ml-2 px-4 py-2 bg-gray-500 text-white rounded-md"
          >
            Search
          </button>
        </div>
        <br />

        {/* 테이블 헤더 */}
        <div className="border-b-2 border-gray-500 py-2 px-4 flex justify-between font-bold bg-gray-200">
          <div className="w-[5%] text-center">ID</div>
          <div className="w-[20%] text-center">Address</div>
          <div className="w-[40%] text-center">Coffees</div>
          <div className="w-[10%] text-center">Total Price</div>
          <div className="w-[10%] text-center">Status</div>
          <div className="w-[5%] text-center">Actions</div>
        </div>

        {/* 주문 목록 렌더링 */}
        <ul className="flex flex-col mt-5">
          {orders.map((order) => (
            <li
              key={order.id}
              className="border-2 border-gray-500 my-2 p-2 flex flex-row justify-between items-center"
            >
              <div className="w-[5%]">{order.id}</div>
              <div className="flex flex-col m-4 justify-between items-center w-[20%]">
                <div>{order.address}</div>
                <div>{new Date(order.orderTime).toLocaleString()}</div>
              </div>
              <ul className="ml-4 w-[40%]">
                {order.orderCoffees.map((coffeeOrder) => (
                  <li
                    key={coffeeOrder.id}
                    className="border p-2 my-1 flex flex-row justify-between items-center"
                  >
                    <div className="flex flex-col justify-between mr-5">
                      <div>{coffeeOrder.coffee.name}</div>
                      <span className="text-gray-600">
                        {coffeeOrder.coffee.price.toLocaleString()} ₩
                      </span>
                    </div>
                    {editingOrder === order.id ? (
                      <div className="flex flex-row items-center">
                        <button
                          className="px-2 py-1 bg-gray-300 rounded-full"
                          onClick={() =>
                            handleDecrease(order.id, coffeeOrder.id)
                          }
                        >
                          -
                        </button>
                        <div className="mx-2">{coffeeOrder.quantity}</div>
                        <button
                          className="px-2 py-1 bg-gray-300 rounded-full"
                          onClick={() =>
                            handleIncrease(order.id, coffeeOrder.id)
                          }
                        >
                          +
                        </button>
                      </div>
                    ) : (
                      <div>{coffeeOrder.quantity}</div>
                    )}
                  </li>
                ))}
              </ul>
              <div className="flex flex-col m-4 justify-between items-center w-[10%]">
                {calculateTotalPrice(order).toLocaleString()} ₩
              </div>
              <div className="flex flex-col m-4 justify-between items-center w-[10%]">
                {order.status ? "출고 완료" : "출고 전"}
              </div>
              <div className="flex flex-col w-[5%]">
                <button
                  className="bg-blue-400 text-white px-2 py-1 rounded-md mb-2"
                  onClick={() => toggleEditing(order.id)}
                >
                  {editingOrder === order.id ? "완료" : "수정"}
                </button>
                <button
                  onClick={() => handleDelete(order.id)}
                  className="bg-red-400 text-white px-2 py-1 rounded-md"
                >
                  삭제
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

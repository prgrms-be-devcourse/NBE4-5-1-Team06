"use client";
import axios from "axios";
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

axios.defaults.baseURL =
  "https://e12668b6-de29-42fa-87ca-38a838a574c9.mock.pstmn.io";

export default function ClientPage() {
  const [email, setEmail] = useState("");
  const [orders, setOrders] = useState<Order[]>([]);

  const getData = async () => {
    try {
      const response = await axios.get(`/api/order/${email}`);
      console.log(response.data);
      setOrders(response.data.orders);
    } catch (error) {
      alert("해당 이메일의 주문은 존재하지 않습니다.");
    }
  };

  // 수량 증가
  const handleIncrease = (orderId: number, coffeeId: number) => {
    setOrders((prevOrders) =>
      prevOrders.map((order) => {
        if (order.id === orderId) {
          return {
            ...order,
            orderCoffees: order.orderCoffees.map((coffeeOrder) => {
              if (coffeeOrder.id === coffeeId) {
                const updatedQuantity = coffeeOrder.quantity + 1;
                return {
                  ...coffeeOrder,
                  quantity: updatedQuantity,
                };
              }
              return coffeeOrder;
            }),
          };
        }
        return order;
      })
    );
  };

  // 수량 감소
  const handleDecrease = (orderId: number, coffeeId: number) => {
    setOrders((prevOrders) =>
      prevOrders.map((order) => {
        if (order.id === orderId) {
          return {
            ...order,
            orderCoffees: order.orderCoffees.map((coffeeOrder) => {
              if (coffeeOrder.id === coffeeId && coffeeOrder.quantity > 1) {
                const updatedQuantity = coffeeOrder.quantity - 1;
                return {
                  ...coffeeOrder,
                  quantity: updatedQuantity,
                };
              }
              return coffeeOrder;
            }),
          };
        }
        return order;
      })
    );
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

  // 서버에 PATCH 요청 보내기
  const updateOrder = async (orderId: number) => {
    try {
      const orderToUpdate = orders.find((order) => order.id === orderId);
      if (!orderToUpdate) return;

      const updatedOrderData = {
        ...orderToUpdate,
        totalPrice: calculateTotalPrice(orderToUpdate), // 총 가격 다시 계산
      };

      // 서버에 PATCH 요청
      const response = await axios.patch(
        `/api/order/${orderId}`,
        updatedOrderData
      );
      console.log("Updated order:", response.data);

      // 상태 업데이트
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.id === orderId ? { ...order, ...updatedOrderData } : order
        )
      );
    } catch (error) {
      alert("수정 실패");
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
      <div className="text-4xl font-bold">Grids & Circles</div>
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

        {/* 주문 목록 렌더링 */}
        <ul className="flex flex-col mt-5">
          {orders.map((order) => (
            <li
              key={order.id}
              className="border-2 border-gray-500 my-2 p-2 flex flex-row justify-between items-center"
            >
              <div>{order.id}</div>
              <div className="flex flex-col m-4 justify-between items-center">
                <div>{order.address}</div>
                <div>{new Date(order.orderTime).toLocaleString()}</div>
              </div>
              <ul className="ml-4">
                {order.orderCoffees.map((coffeeOrder) => (
                  <li
                    key={coffeeOrder.id}
                    className="border p-2 my-1 flex flex-row justify-between items-center"
                  >
                    <div className="flex flex-col justify-between mr-5">
                      <div>☕ {coffeeOrder.coffee.name}</div>
                      <div>
                        💵 Price: {coffeeOrder.coffee.price.toLocaleString()} 원
                      </div>
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
              <div>
                💰 Total Price: {calculateTotalPrice(order).toLocaleString()} 원
              </div>
              <div>🚀 Status: {order.status ? "Completed" : "Pending"}</div>
              <div className="flex flex-col">
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

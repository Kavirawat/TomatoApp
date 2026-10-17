import React, { useState, useEffect } from 'react';
import {
  FaSyncAlt,
  FaCheckCircle,
  FaExclamationTriangle,
  FaBell,
  FaUtensils,
  FaMotorcycle,
  FaBoxes,
  FaClock,
  FaMapMarkerAlt,
  FaKey,
  FaLock,
  FaMobileAlt,
  FaComments,
  FaCommentDots,
  FaPaperPlane,
  FaTimes,
  FaUser,
  FaStore,
  FaBullhorn,
  FaPlusCircle,
  FaReceipt,
  FaCircle,
  FaCheck,
  FaSpinner,
  FaPhoneAlt
} from 'react-icons/fa';

// Connect to backend URL from environment variables (.env) with fallback
const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://127.0.0.1:5000/api';

export default function App() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [serverOnline, setServerOnline] = useState(true);
  const [activeTab, setActiveTab] = useState('ALL'); // ALL, LIVE, PREPARING, OUT_FOR_DELIVERY, DELIVERED
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [chatMessages, setChatMessages] = useState([]);
  const [chatInput, setChatInput] = useState('');
  const [chatLoading, setChatLoading] = useState(false);
  const [otpInputs, setOtpInputs] = useState({});
  const [otpError, setOtpError] = useState({});
  const [statusMessage, setStatusMessage] = useState('');
  const [showCustomerSimulator, setShowCustomerSimulator] = useState(false);

  // Quick preset chat replies for Admin
  const quickReplies = [
    'Chef is cooking your fresh meal in the kitchen.',
    'Food is packed! Rider has picked it up and is en route.',
    'Please keep your 4-digit OTP ready to share with the rider.',
    'Noted! Extra spicy instructions forwarded to the chef.',
    'Extra cutlery, napkins, and dips have been packed.'
  ];

  const [lastKnownOrderCount, setLastKnownOrderCount] = useState(0);
  const [newOrderAlert, setNewOrderAlert] = useState(null);
  const [customNoteInputs, setCustomNoteInputs] = useState({});

  // Play audio chime for new incoming order from app
  const playNewOrderChime = () => {
    try {
      const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
      const osc = audioCtx.createOscillator();
      const gain = audioCtx.createGain();
      osc.connect(gain);
      gain.connect(audioCtx.destination);
      osc.type = 'sine';
      osc.frequency.setValueAtTime(587.33, audioCtx.currentTime); // D5
      osc.frequency.setValueAtTime(880, audioCtx.currentTime + 0.15); // A5
      gain.gain.setValueAtTime(0.2, audioCtx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.4);
      osc.start();
      osc.stop(audioCtx.currentTime + 0.4);
    } catch (e) {
      // AudioContext may require user interaction first
    }
  };

  // Fetch orders from backend
  const fetchOrders = async () => {
    try {
      const res = await fetch(`${API_BASE}/orders`);
      if (!res.ok) throw new Error('Failed to fetch orders');
      const data = await res.json();
      if (data.success && Array.isArray(data.data)) {
        // Detect new order placed from mobile app
        if (lastKnownOrderCount > 0 && data.data.length > lastKnownOrderCount) {
          const newest = data.data[0];
          playNewOrderChime();
          setNewOrderAlert(newest);
          setSelectedOrderId(newest.id);
          setStatusMessage(`New order #${newest.id} received from customer!`);
          setTimeout(() => setNewOrderAlert(null), 10000);
        }
        setLastKnownOrderCount(data.data.length);
        setOrders(data.data);
        setServerOnline(true);
        if (!selectedOrderId && data.data.length > 0) {
          setSelectedOrderId(data.data[0].id);
        }
      }
    } catch (err) {
      setServerOnline(false);
    } finally {
      setLoading(false);
    }
  };

  // User-triggered Refresh with spinning animation and visual feedback
  const handleRefresh = async () => {
    setIsRefreshing(true);
    try {
      await fetchOrders();
      setStatusMessage('Orders refreshed');
      setTimeout(() => setStatusMessage(''), 2500);
    } catch (err) {
      // Handled in fetchOrders
    } finally {
      setTimeout(() => {
        setIsRefreshing(false);
      }, 500);
    }
  };

  // Fetch chat for selected order
  const fetchChat = async (orderId) => {
    if (!orderId) return;
    try {
      const res = await fetch(`${API_BASE}/orders/${orderId}/chat`);
      if (res.ok) {
        const data = await res.json();
        if (data.success) {
          setChatMessages(data.data || []);
        }
      }
    } catch (err) {
      // Chat fetch error
    }
  };

  // Real-time polling: 1.5s when online, backs off to 5s when offline
  useEffect(() => {
    fetchOrders();
    const intervalTime = serverOnline ? 1500 : 5000;
    const interval = setInterval(() => {
      fetchOrders();
    }, intervalTime);
    return () => clearInterval(interval);
  }, [selectedOrderId, lastKnownOrderCount, serverOnline]);

  // Poll chat when an order is selected and backend is online
  useEffect(() => {
    if (selectedOrderId && serverOnline) {
      fetchChat(selectedOrderId);
      const chatInterval = setInterval(() => {
        fetchChat(selectedOrderId);
      }, 2000);
      return () => clearInterval(chatInterval);
    }
  }, [selectedOrderId, serverOnline]);

  // Clickable: Create New Test Demo Order
  const handleCreateTestOrder = async () => {
    const demoMenuItems = [
      { id: 'm1', name: 'Butter Chicken Meal Combo', price: 299, quantity: 1 },
      { id: 'm2', name: 'Garlic Naan (2 pcs)', price: 80, quantity: 2 },
      { id: 'm3', name: 'Gulab Jamun with Rabri', price: 110, quantity: 1 }
    ];
    const generatedOtp = String(Math.floor(1000 + Math.random() * 9000));
    const testOrderId = `TOM-${Math.floor(1000 + Math.random() * 9000)}`;

    try {
      const res = await fetch(`${API_BASE}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: testOrderId,
          restaurantName: "Haldiram's Sweets & Meals",
          restaurantAddress: "Sector 18, Noida",
          items: demoMenuItems,
          itemTotal: 569,
          deliveryFee: 35,
          taxes: 28,
          discount: 50,
          totalAmount: 582,
          deliveryAddress: "Home • Flat 301, Tower A, Sector 62, Noida",
          deliveryOtp: generatedOtp,
          receiverName: "Kavi Rawat",
          status: "PLACED",
          adminNotes: "Order accepted by kitchen."
        })
      });
      const data = await res.json();
      if (data.success) {
        setSelectedOrderId(testOrderId);
        setStatusMessage(`New test order #${testOrderId} created!`);
        setTimeout(() => setStatusMessage(''), 4000);
        fetchOrders();
      }
    } catch (err) {
      console.error('Error creating test order:', err);
      alert('Backend connection error. Make sure TomatoDEMOServer is running on port 5000.');
    }
  };

  // Clickable: Admin status & situation updates
  const handleUpdateStatus = async (orderId, status, customNote) => {
    try {
      const res = await fetch(`${API_BASE}/orders/${orderId}/status`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status, adminNotes: customNote })
      });
      const data = await res.json();
      if (data.success) {
        const situationName = status === 'PREPARING' ? 'Preparing' : status === 'OUT_FOR_DELIVERY' ? 'Out for Delivery' : 'Delivered';
        setStatusMessage(`Order #${orderId} updated: ${situationName}`);
        setTimeout(() => setStatusMessage(''), 3000);
        fetchOrders();
      } else {
        alert(data.message || 'Status update failed');
      }
    } catch (err) {
      console.error('Error updating status:', err);
      alert('Failed to connect to backend server on port 5000');
    }
  };

  // Clickable: Auto-fill OTP into input box
  const handleAutoFillOtp = (orderId, otp) => {
    setOtpInputs(prev => ({ ...prev, [orderId]: otp }));
    setOtpError(prev => ({ ...prev, [orderId]: null }));
  };

  // Clickable: OTP verification by Admin/Rider
  const handleVerifyOtp = async (orderId) => {
    const enteredOtp = otpInputs[orderId];
    if (!enteredOtp || enteredOtp.trim().length !== 4) {
      setOtpError(prev => ({ ...prev, [orderId]: 'Please enter 4-digit delivery OTP' }));
      return;
    }

    try {
      const res = await fetch(`${API_BASE}/orders/${orderId}/verify-otp`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ otp: enteredOtp.trim() })
      });
      const data = await res.json();
      if (data.success) {
        setOtpError(prev => ({ ...prev, [orderId]: null }));
        setStatusMessage(`OTP Verified! Order #${orderId} marked as DELIVERED.`);
        setTimeout(() => setStatusMessage(''), 4000);
        setOtpInputs(prev => ({ ...prev, [orderId]: '' }));
        fetchOrders();
      } else {
        setOtpError(prev => ({ ...prev, [orderId]: data.message || 'Incorrect OTP' }));
      }
    } catch (err) {
      console.error('Error verifying OTP:', err);
      setOtpError(prev => ({ ...prev, [orderId]: 'Server error verifying OTP' }));
    }
  };

  // Clickable: Send Chat message from Admin
  const handleSendChat = async (e) => {
    if (e) e.preventDefault();
    if (!chatInput.trim() || !selectedOrderId || chatLoading) return;
    const currentOrder = orders.find(o => o.id === selectedOrderId);
    if (currentOrder?.status === 'DELIVERED') return;

    setChatLoading(true);
    try {
      const res = await fetch(`${API_BASE}/orders/${selectedOrderId}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          message: chatInput.trim(),
          sender: 'admin',
          senderName: 'Admin Support'
        })
      });
      if (res.ok) {
        setChatInput('');
        fetchChat(selectedOrderId);
      }
    } catch (err) {
      console.error('Error sending chat message:', err);
    } finally {
      setChatLoading(false);
    }
  };

  // Clickable: Send quick preset reply
  const handleSendQuickReply = async (presetText) => {
    if (!selectedOrderId || chatLoading) return;
    const currentOrder = orders.find(o => o.id === selectedOrderId);
    if (currentOrder?.status === 'DELIVERED') return;

    setChatLoading(true);
    try {
      const res = await fetch(`${API_BASE}/orders/${selectedOrderId}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          message: presetText,
          sender: 'admin',
          senderName: 'Admin Support'
        })
      });
      if (res.ok) {
        fetchChat(selectedOrderId);
      }
    } catch (err) {
      console.error('Error sending quick reply:', err);
    } finally {
      setChatLoading(false);
    }
  };

  // Filtered orders
  const filteredOrders = orders.filter(order => {
    if (activeTab === 'LIVE') return order.status !== 'DELIVERED';
    if (activeTab === 'PREPARING') return order.status === 'PREPARING';
    if (activeTab === 'OUT_FOR_DELIVERY') return order.status === 'OUT_FOR_DELIVERY';
    if (activeTab === 'DELIVERED') return order.status === 'DELIVERED';
    return true;
  });

  // Calculate statistics
  const totalOrdersCount = orders.length;
  const preparingCount = orders.filter(o => o.status === 'PREPARING').length;
  const outDeliveryCount = orders.filter(o => o.status === 'OUT_FOR_DELIVERY').length;
  const deliveredCount = orders.filter(o => o.status === 'DELIVERED').length;

  const selectedOrder = orders.find(o => o.id === selectedOrderId);

  return (
    <div>
      {/* Top Navigation */}
      <header className="admin-navbar">
        <div className="brand-container">
          <span className="brand-logo">tomato</span>
          <span className="brand-badge">TomatoDEMOAdmin • Kitchen & Partner Portal</span>
        </div>

        <div className="nav-actions">
          {/* Strictly shows "Online" or "Offline" per user requirement */}
          <div
            className="server-status"
            style={{
              background: serverOnline ? '#F0FDF4' : '#FEF2F2',
              color: serverOnline ? '#15803D' : '#B91C1C',
              borderColor: serverOnline ? '#BBF7D0' : '#FECACA',
              cursor: 'pointer'
            }}
            onClick={handleRefresh}
            title={serverOnline ? 'Online • Click to refresh' : 'Offline • Click to reconnect'}
          >
            <span className="status-dot" style={{ background: serverOnline ? '#22C55E' : '#EF4444' }}></span>
            {serverOnline ? 'Online' : 'Offline'}
          </div>

          {/* Customer App Simulator Toggle */}
          <button
            className="refresh-btn"
            style={{
              background: showCustomerSimulator ? '#1E293B' : '#FFFFFF',
              color: showCustomerSimulator ? '#FFFFFF' : '#1E293B'
            }}
            onClick={() => setShowCustomerSimulator(!showCustomerSimulator)}
            title="Preview customer mobile view"
          >
            <FaMobileAlt style={{ fontSize: '13px' }} />
            <span>{showCustomerSimulator ? 'Hide App View' : 'Customer App Preview'}</span>
          </button>

          {/* Working Refresh Button with FontAwesome 5 Spin Feedback */}
          <button
            className="refresh-btn"
            onClick={handleRefresh}
            disabled={isRefreshing}
            title="Refresh orders list"
          >
            <FaSyncAlt className={isRefreshing ? 'spin-icon' : ''} style={{ fontSize: '12px' }} />
            <span>Refresh</span>
          </button>
        </div>
      </header>

      {/* Backend Offline Warning Banner */}
      {!serverOnline && (
        <div style={{
          maxWidth: '1300px',
          margin: '16px auto 0',
          padding: '14px 20px',
          background: '#FFFBEB',
          border: '1.5px solid #F59E0B',
          borderRadius: '12px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          gap: '16px',
          flexWrap: 'wrap',
          boxShadow: '0 2px 8px rgba(245, 158, 11, 0.15)'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <FaExclamationTriangle style={{ fontSize: '22px', color: '#D97706' }} />
            <div>
              <strong style={{ color: '#B45309', fontSize: '14px' }}>
                Server is currently disconnected on port 5000
              </strong>
              <div style={{ fontSize: '13px', color: '#78350F', marginTop: '3px' }}>
                Please ensure TomatoDEMOServer is running: <code style={{ background: '#FEF3C7', padding: '2px 8px', borderRadius: '4px', fontWeight: 'bold' }}>cd TomatoDEMOServer && npm start</code>
              </div>
            </div>
          </div>
          <button
            onClick={handleRefresh}
            disabled={isRefreshing}
            style={{
              background: '#D97706',
              color: '#FFFFFF',
              border: 'none',
              padding: '8px 16px',
              borderRadius: '6px',
              fontWeight: '600',
              cursor: 'pointer',
              display: 'inline-flex',
              alignItems: 'center',
              gap: '6px'
            }}
          >
            <FaSyncAlt className={isRefreshing ? 'spin-icon' : ''} style={{ fontSize: '12px' }} />
            <span>Check Connection</span>
          </button>
        </div>
      )}

      {/* Floating Status Notification */}
      {statusMessage && (
        <div style={{
          position: 'fixed',
          top: '75px',
          right: '24px',
          background: '#15803D',
          color: '#FFFFFF',
          padding: '10px 18px',
          borderRadius: '8px',
          fontSize: '13px',
          fontWeight: '600',
          boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
          zIndex: 99,
          display: 'flex',
          alignItems: 'center',
          gap: '8px'
        }}>
          <FaCheckCircle />
          <span>{statusMessage}</span>
        </div>
      )}

      {/* High-Visibility New Order Incoming Banner */}
      {newOrderAlert && (
        <div style={{
          maxWidth: '1300px',
          margin: '16px auto 0',
          padding: '14px 20px',
          background: '#FEF2F2',
          border: '2px solid #E23744',
          borderRadius: '12px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          boxShadow: '0 4px 14px rgba(226, 55, 68, 0.2)',
          flexWrap: 'wrap',
          gap: '12px'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <FaBell style={{ fontSize: '24px', color: '#E23744' }} />
            <div>
              <strong style={{ color: '#E23744', fontSize: '15px', display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
                <span>NEW ORDER: {newOrderAlert.receiverName || 'Customer'}</span>
                <span style={{ fontSize: '11px', fontWeight: 700, color: '#DC2626', background: '#FEE2E2', border: '1px solid #FECACA', padding: '1px 6px', borderRadius: '4px' }}>
                  #{newOrderAlert.id}
                </span>
              </strong>
              <div style={{ fontSize: '12px', color: '#4B5563', marginTop: '3px' }}>
                Customer: <strong>{newOrderAlert.receiverName || 'Customer'}</strong> • Restaurant: <strong>{newOrderAlert.restaurantName}</strong> • Total: <strong>₹{newOrderAlert.totalAmount}</strong> • OTP: <strong style={{ color: '#047857', background: '#DCFCE7', padding: '1px 6px', borderRadius: '4px' }}>{newOrderAlert.deliveryOtp}</strong>
              </div>
            </div>
          </div>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              onClick={() => handleUpdateStatus(newOrderAlert.id, 'PREPARING')}
              style={{
                background: '#EA580C',
                color: '#FFFFFF',
                padding: '8px 14px',
                borderRadius: '8px',
                fontSize: '12px',
                fontWeight: '700',
                cursor: 'pointer',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '6px'
              }}
            >
              <FaUtensils />
              <span>Accept Order</span>
            </button>
            <button
              onClick={() => handleUpdateStatus(newOrderAlert.id, 'OUT_FOR_DELIVERY')}
              style={{
                background: '#2563EB',
                color: '#FFFFFF',
                padding: '8px 14px',
                borderRadius: '8px',
                fontSize: '12px',
                fontWeight: '700',
                cursor: 'pointer',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '6px'
              }}
            >
              <FaMotorcycle />
              <span>Dispatch Rider</span>
            </button>
            <button
              onClick={() => setNewOrderAlert(null)}
              style={{ background: '#E5E7EB', color: '#374151', padding: '8px 12px', borderRadius: '8px', fontSize: '12px', fontWeight: '600', cursor: 'pointer' }}
            >
              <FaTimes />
            </button>
          </div>
        </div>
      )}

      {/* Professional Statistics Cards with FontAwesome 5 Icons */}
      <section className="stats-container">
        <div
          className="stat-card"
          style={{ cursor: 'pointer', border: activeTab === 'ALL' ? '2px solid #E23744' : '1px solid #E5E7EB' }}
          onClick={() => setActiveTab('ALL')}
        >
          <div className="stat-icon" style={{ background: '#FEE2E2', color: '#DC2626' }}>
            <FaBoxes />
          </div>
          <div className="stat-info">
            <h4>Total Orders</h4>
            <p>{totalOrdersCount}</p>
          </div>
        </div>

        <div
          className="stat-card"
          style={{ cursor: 'pointer', border: activeTab === 'PREPARING' ? '2px solid #EA580C' : '1px solid #E5E7EB' }}
          onClick={() => setActiveTab('PREPARING')}
        >
          <div className="stat-icon" style={{ background: '#FFEDD5', color: '#EA580C' }}>
            <FaUtensils />
          </div>
          <div className="stat-info">
            <h4>Preparing</h4>
            <p>{preparingCount}</p>
          </div>
        </div>

        <div
          className="stat-card"
          style={{ cursor: 'pointer', border: activeTab === 'OUT_FOR_DELIVERY' ? '2px solid #2563EB' : '1px solid #E5E7EB' }}
          onClick={() => setActiveTab('OUT_FOR_DELIVERY')}
        >
          <div className="stat-icon" style={{ background: '#DBEAFE', color: '#2563EB' }}>
            <FaMotorcycle />
          </div>
          <div className="stat-info">
            <h4>Out for Delivery</h4>
            <p>{outDeliveryCount}</p>
          </div>
        </div>

        <div
          className="stat-card"
          style={{ cursor: 'pointer', border: activeTab === 'DELIVERED' ? '2px solid #16A34A' : '1px solid #E5E7EB' }}
          onClick={() => setActiveTab('DELIVERED')}
        >
          <div className="stat-icon" style={{ background: '#DCFCE7', color: '#16A34A' }}>
            <FaCheckCircle />
          </div>
          <div className="stat-info">
            <h4>Delivered</h4>
            <p>{deliveredCount}</p>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="main-content" style={{ gridTemplateColumns: showCustomerSimulator ? '1fr 340px 320px' : '1fr 380px' }}>
        {/* Left: Orders List */}
        <div className="orders-section">
          <div className="orders-header">
            <div className="tab-group">
              <button
                className={`tab-btn ${activeTab === 'ALL' ? 'active' : ''}`}
                onClick={() => setActiveTab('ALL')}
              >
                All ({orders.length})
              </button>
              <button
                className={`tab-btn ${activeTab === 'LIVE' ? 'active' : ''}`}
                onClick={() => setActiveTab('LIVE')}
              >
                <FaCircle style={{ color: '#EF4444', fontSize: '8px', marginRight: '6px' }} />
                Live ({preparingCount + outDeliveryCount})
              </button>
              <button
                className={`tab-btn ${activeTab === 'PREPARING' ? 'active' : ''}`}
                onClick={() => setActiveTab('PREPARING')}
              >
                <FaUtensils style={{ fontSize: '11px', marginRight: '6px' }} />
                Preparing ({preparingCount})
              </button>
              <button
                className={`tab-btn ${activeTab === 'OUT_FOR_DELIVERY' ? 'active' : ''}`}
                onClick={() => setActiveTab('OUT_FOR_DELIVERY')}
              >
                <FaMotorcycle style={{ fontSize: '12px', marginRight: '6px' }} />
                Out for Delivery ({outDeliveryCount})
              </button>
              <button
                className={`tab-btn ${activeTab === 'DELIVERED' ? 'active' : ''}`}
                onClick={() => setActiveTab('DELIVERED')}
              >
                <FaCheckCircle style={{ fontSize: '11px', marginRight: '6px' }} />
                Delivered ({deliveredCount})
              </button>
            </div>
          </div>

          {loading ? (
            <div className="empty-orders">
              <FaSpinner className="spin-icon" style={{ fontSize: '36px', color: '#9CA3AF', margin: '0 auto 12px' }} />
              <h3>Loading orders...</h3>
            </div>
          ) : filteredOrders.length === 0 ? (
            <div className="empty-orders">
              <FaReceipt style={{ fontSize: '42px', color: '#CBD5E1', margin: '0 auto 12px' }} />
              <h3>No orders in this category</h3>
              <p>Place an order from the Tomato mobile app or create a demo test order below.</p>
              <button
                className="refresh-btn"
                style={{ background: '#E23744', color: '#FFFFFF', margin: '14px auto 0' }}
                onClick={handleCreateTestOrder}
              >
                <FaPlusCircle />
                <span>Create Test Order</span>
              </button>
            </div>
          ) : (
            filteredOrders.map((order) => {
              const isSelected = order.id === selectedOrderId;
              const statusClass = order.status.toLowerCase();

              return (
                <div
                  key={order.id}
                  className={`order-card ${isSelected ? 'selected' : ''}`}
                  onClick={() => {
                    setSelectedOrderId(order.id);
                    fetchChat(order.id);
                  }}
                  style={{ cursor: 'pointer' }}
                >
                  {/* Clean Professional Order Header */}
                  <div className="order-header">
                    <div className="order-id-block">
                      <h3 style={{ display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
                        <span style={{ fontSize: '16px', fontWeight: 800, color: '#111827' }}>
                          {order.receiverName && order.receiverName !== 'Customer' ? order.receiverName : 'Kavi Rawat'}
                        </span>
                        <span style={{
                          fontSize: '11px',
                          fontWeight: 700,
                          color: '#64748B',
                          background: '#F1F5F9',
                          border: '1px solid #E2E8F0',
                          padding: '1px 6px',
                          borderRadius: '4px',
                          letterSpacing: '0.3px'
                        }}>
                          #{order.id}
                        </span>
                        <span style={{ fontSize: '13px', color: '#6B7280', fontWeight: 500 }}>
                          • {order.restaurantName}
                        </span>
                        {isSelected && (
                          <span style={{ fontSize: '10px', background: '#FEE2E2', color: '#DC2626', padding: '2px 7px', borderRadius: '4px', fontWeight: 700, display: 'inline-flex', alignItems: 'center', gap: '3px' }}>
                            <FaCheck style={{ fontSize: '8px' }} /> SELECTED
                          </span>
                        )}
                      </h3>
                      <p style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <FaClock style={{ fontSize: '11px', color: '#9CA3AF' }} />
                        <span>Ordered at {order.orderedAt || 'Recent'}</span>
                        <span>•</span>
                        <span>ETA: ~{order.etaMinutes} mins</span>
                      </p>
                    </div>

                    <span className={`status-badge ${statusClass}`}>
                      {order.status === 'PREPARING' && (
                        <>
                          <FaUtensils style={{ fontSize: '11px' }} />
                          <span>Preparing</span>
                        </>
                      )}
                      {order.status === 'OUT_FOR_DELIVERY' && (
                        <>
                          <FaMotorcycle style={{ fontSize: '12px' }} />
                          <span>Out for Delivery</span>
                        </>
                      )}
                      {order.status === 'DELIVERED' && (
                        <>
                          <FaCheckCircle style={{ fontSize: '11px' }} />
                          <span>Delivered</span>
                        </>
                      )}
                      {order.status === 'PLACED' && (
                        <>
                          <FaClock style={{ fontSize: '11px' }} />
                          <span>Order Placed</span>
                        </>
                      )}
                    </span>
                  </div>

                  {/* Clean Situation banner */}
                  {order.adminNotes && (
                    <div className="situation-banner">
                      <FaBullhorn style={{ color: '#D97706', fontSize: '13px', flexShrink: 0 }} />
                      <div>
                        <strong>Status Update:</strong> {order.adminNotes}
                      </div>
                    </div>
                  )}

                  {/* Clean Professional Order Body */}
                  <div className="order-body">
                    {/* Items List */}
                    <div className="items-list">
                      <h4 style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <FaReceipt style={{ fontSize: '12px' }} />
                        <span>Items Ordered ({order.items?.length || 0})</span>
                      </h4>
                      {order.items?.map((item, idx) => (
                        <div key={idx} className="item-row">
                          <span>{item.name} × {item.quantity}</span>
                          <span style={{ fontWeight: 600 }}>₹{item.price * item.quantity}</span>
                        </div>
                      ))}
                      <div className="order-total-row">
                        <span>Total Paid</span>
                        <span style={{ color: '#E23744' }}>₹{order.totalAmount}</span>
                      </div>
                    </div>

                    {/* Delivery & Customer Info */}
                    <div className="delivery-info">
                      <h4 style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <FaUser style={{ fontSize: '12px' }} />
                        <span>Customer & Delivery Details</span>
                      </h4>
                      <p className="address-text" style={{ display: 'flex', alignItems: 'flex-start', gap: '6px' }}>
                        <FaMapMarkerAlt style={{ color: '#EF4444', fontSize: '13px', marginTop: '3px', flexShrink: 0 }} />
                        <span><strong>Address:</strong> {order.deliveryAddress}</span>
                      </p>
                      <p className="partner-text" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <FaMotorcycle style={{ color: '#2563EB', fontSize: '13px', flexShrink: 0 }} />
                        <span><strong>Rider:</strong> {order.deliveryPartnerName} ({order.deliveryPartnerPhone})</span>
                      </p>
                    </div>
                  </div>

                  {/* Decluttered Professional Action Controls */}
                  <div className="order-controls" onClick={(e) => e.stopPropagation()}>
                    {/* Status Management Row */}
                    <div className="action-buttons-row">
                      {order.status !== 'DELIVERED' && (
                        <>
                          <button
                            className="btn-status-prep"
                            onClick={() => handleUpdateStatus(order.id, 'PREPARING', 'Kitchen preparing meal fresh.')}
                            title="Update status to Preparing"
                          >
                            <FaUtensils />
                            <span>Mark Preparing</span>
                          </button>
                          <button
                            className="btn-status-rider"
                            onClick={() => handleUpdateStatus(order.id, 'OUT_FOR_DELIVERY', 'Order picked up by rider and out for delivery.')}
                            title="Update status to Out for Delivery"
                          >
                            <FaMotorcycle />
                            <span>Mark Out for Delivery</span>
                          </button>
                        </>
                      )}

                      <button
                        className="btn-chat"
                        onClick={() => {
                          setSelectedOrderId(order.id);
                          fetchChat(order.id);
                        }}
                      >
                        <FaCommentDots />
                        <span>{isSelected ? 'Live Chat Active' : 'Open Live Chat'}</span>
                      </button>
                    </div>

                    {/* Streamlined Customer Status Note Input */}
                    {order.status !== 'DELIVERED' && (
                      <div style={{ display: 'flex', gap: '8px', marginTop: '2px' }}>
                        <input
                          type="text"
                          placeholder="Send quick status note to customer app (e.g. Packing done, Rider at gate)..."
                          value={customNoteInputs[order.id] || ''}
                          onChange={(e) => setCustomNoteInputs({ ...customNoteInputs, [order.id]: e.target.value })}
                          style={{
                            flex: 1,
                            padding: '8px 12px',
                            border: '1px solid #CBD5E1',
                            borderRadius: '6px',
                            fontSize: '12px'
                          }}
                        />
                        <button
                          type="button"
                          onClick={() => {
                            const note = customNoteInputs[order.id];
                            if (note && note.trim()) {
                              handleUpdateStatus(order.id, order.status, note.trim());
                              setCustomNoteInputs({ ...customNoteInputs, [order.id]: '' });
                            }
                          }}
                          style={{
                            background: '#334155',
                            color: '#FFFFFF',
                            padding: '8px 14px',
                            borderRadius: '6px',
                            fontSize: '11px',
                            fontWeight: '700',
                            cursor: 'pointer',
                            display: 'inline-flex',
                            alignItems: 'center',
                            gap: '6px'
                          }}
                          title="Post real-time update note to customer app"
                        >
                          <FaBullhorn />
                          <span>Send Update</span>
                        </button>
                      </div>
                    )}

                    {/* Professional OTP Verification Bar */}
                    <div className="otp-verify-box">
                      <div className="otp-box-left">
                        <FaLock style={{ color: order.isOtpVerified ? '#16A34A' : '#D97706', fontSize: '15px' }} />
                        <div>
                          <div className="otp-hint">
                            {order.isOtpVerified ? (
                              <span style={{ background: '#DCFCE7', color: '#15803D', display: 'inline-flex', alignItems: 'center', gap: '5px' }}>
                                <FaCheckCircle /> OTP Verified & Delivered
                              </span>
                            ) : (
                              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
                                <span>Customer Delivery OTP: <strong>{order.deliveryOtp}</strong></span>
                                <button
                                  type="button"
                                  className="btn-autofill"
                                  onClick={() => handleAutoFillOtp(order.id, order.deliveryOtp)}
                                  title="Auto-fill OTP for testing"
                                >
                                  <FaCheck style={{ fontSize: '9px' }} /> Auto Fill
                                </button>
                              </div>
                            )}
                          </div>
                          {otpError[order.id] && (
                            <div style={{ color: '#DC2626', fontSize: '11px', fontWeight: 600, marginTop: '3px', display: 'flex', alignItems: 'center', gap: '4px' }}>
                              <FaExclamationTriangle style={{ fontSize: '11px' }} />
                              <span>{otpError[order.id]}</span>
                            </div>
                          )}
                        </div>
                      </div>

                      {order.isOtpVerified ? (
                        <div className="otp-delivered-badge">
                          <FaCheckCircle />
                          <span>Delivered</span>
                        </div>
                      ) : (
                        <div className="otp-form">
                          <input
                            type="text"
                            maxLength={4}
                            placeholder="4-digit OTP"
                            className="otp-input"
                            value={otpInputs[order.id] || ''}
                            onChange={(e) => {
                              const val = e.target.value.replace(/\D/g, '');
                              setOtpInputs(prev => ({ ...prev, [order.id]: val }));
                            }}
                          />
                          <button
                            className="btn-verify-otp"
                            onClick={() => handleVerifyOtp(order.id)}
                            style={{ display: 'inline-flex', alignItems: 'center', gap: '6px' }}
                          >
                            <FaCheckCircle />
                            <span>Verify & Complete</span>
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })
          )}
        </div>

        {/* Center/Right: Live Customer Chat Panel */}
        <aside className="chat-panel">
          <div className="chat-panel-header">
            <div>
              <h3 style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <FaComments style={{ color: '#E23744' }} />
                <span>Customer Live Chat</span>
              </h3>
              <p style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap' }}>
                {selectedOrder ? (
                  <>
                    <strong style={{ color: '#111827' }}>
                      {selectedOrder.receiverName && selectedOrder.receiverName !== 'Customer' ? selectedOrder.receiverName : 'Kavi Rawat'}
                    </strong>
                    <span style={{ fontSize: '11px', fontWeight: 600, color: '#64748B', background: '#F1F5F9', border: '1px solid #CBD5E1', padding: '1px 5px', borderRadius: '4px' }}>
                      #{selectedOrder.id}
                    </span>
                    <span>• {selectedOrder.restaurantName}</span>
                  </>
                ) : (
                  'Select an order to chat'
                )}
              </p>
            </div>
            {selectedOrder && (
              <span className="status-badge preparing" style={{ fontSize: '10px' }}>
                Active
              </span>
            )}
          </div>

          {/* Quick Preset Reply Chips for Admin */}
          {selectedOrderId && selectedOrder?.status !== 'DELIVERED' && (
            <div style={{
              padding: '8px 12px',
              background: '#F1F5F9',
              borderBottom: '1px solid #E2E8F0',
              display: 'flex',
              gap: '6px',
              overflowX: 'auto',
              whiteSpace: 'nowrap'
            }}>
              {quickReplies.map((reply, idx) => (
                <button
                  key={idx}
                  type="button"
                  onClick={() => handleSendQuickReply(reply)}
                  style={{
                    background: '#FFFFFF',
                    border: '1px solid #CBD5E1',
                    borderRadius: '12px',
                    padding: '5px 10px',
                    fontSize: '11px',
                    color: '#334155',
                    cursor: 'pointer'
                  }}
                  title="Click to send quick response"
                >
                  {reply.length > 30 ? reply.substring(0, 30) + '...' : reply}
                </button>
              ))}
            </div>
          )}

          <div className="chat-messages">
            {!selectedOrderId ? (
              <div style={{ textAlign: 'center', color: '#9CA3AF', margin: 'auto' }}>
                Select an order card to start messaging the customer.
              </div>
            ) : chatMessages.length === 0 ? (
              <div style={{ textAlign: 'center', color: '#9CA3AF', margin: 'auto' }}>
                No messages yet. Send an update to the customer!
              </div>
            ) : (
              chatMessages.map((msg) => (
                <div
                  key={msg.id}
                  className={`chat-bubble ${msg.sender === 'admin' ? 'admin' : 'user'}`}
                >
                  <div className="chat-sender-name" style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    {msg.sender === 'admin' ? (
                      <>
                        <FaStore style={{ fontSize: '9px' }} />
                        <span>Store Manager</span>
                      </>
                    ) : (
                      <>
                        <FaUser style={{ fontSize: '9px' }} />
                        <span>{msg.senderName || 'Customer'}</span>
                      </>
                    )}
                  </div>
                  <p>{msg.message}</p>
                  <div className="chat-time">{msg.time || 'Just now'}</div>
                </div>
              ))
            )}
          </div>

          {selectedOrderId && (
            selectedOrder?.status === 'DELIVERED' ? (
              <div style={{
                padding: '14px 16px',
                background: '#F1F5F9',
                borderTop: '1px solid #CBD5E1',
                textAlign: 'center',
                color: '#64748B',
                fontSize: '12px',
                fontWeight: '700',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '6px'
              }}>
                <FaLock />
                <span>Order Delivered Successfully • Chat Closed</span>
              </div>
            ) : (
              <form className="chat-input-area" onSubmit={handleSendChat}>
                <input
                  type="text"
                  placeholder="Type reply to customer..."
                  className="chat-input"
                  value={chatInput}
                  onChange={(e) => setChatInput(e.target.value)}
                />
                <button type="submit" className="btn-send-chat" disabled={chatLoading} style={{ display: 'inline-flex', alignItems: 'center', gap: '6px' }}>
                  {chatLoading ? <FaSpinner className="spin-icon" /> : <FaPaperPlane />}
                  <span>Send</span>
                </button>
              </form>
            )
          )}
        </aside>

        {/* Optional Right Column: Customer Mobile App Preview */}
        {showCustomerSimulator && (
          <aside style={{
            background: '#FFFFFF',
            borderRadius: '16px',
            border: '2px solid #CBD5E1',
            boxShadow: '0 8px 24px rgba(0,0,0,0.1)',
            overflow: 'hidden',
            height: '600px',
            display: 'flex',
            flexDirection: 'column',
            position: 'sticky',
            top: '90px'
          }}>
            {/* Phone Frame Header */}
            <div style={{
              background: '#1E293B',
              color: '#FFFFFF',
              padding: '10px 14px',
              fontSize: '12px',
              fontWeight: '700',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: '6px' }}>
                <FaMobileAlt /> Customer Mobile View
              </span>
              <button
                type="button"
                onClick={() => setShowCustomerSimulator(false)}
                style={{ background: 'transparent', color: '#94A3B8', fontSize: '14px', cursor: 'pointer' }}
              >
                <FaTimes />
              </button>
            </div>

            {selectedOrder ? (
              <div style={{ padding: '14px', overflowY: 'auto', flex: 1, background: '#F8FAFC' }}>
                {/* Simulated Tomato App Header */}
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  background: '#FFFFFF',
                  padding: '8px 12px',
                  borderRadius: '10px',
                  border: '1px solid #E2E8F0',
                  marginBottom: '12px'
                }}>
                  <span style={{ color: '#E23744', fontWeight: '900', fontStyle: 'italic', fontSize: '18px' }}>tomato</span>
                  <span style={{ fontSize: '11px', color: '#64748B', fontWeight: '600', display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                    <FaMapMarkerAlt style={{ color: '#E23744' }} /> Delhi NCR
                  </span>
                </div>

                {/* Customer Name & Order ID Badge */}
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  background: '#FFFFFF',
                  padding: '8px 12px',
                  borderRadius: '10px',
                  border: '1px solid #E2E8F0',
                  marginBottom: '12px'
                }}>
                  <strong style={{ fontSize: '13px', color: '#1E293B' }}>
                    {selectedOrder.receiverName && selectedOrder.receiverName !== 'Customer' ? selectedOrder.receiverName : 'Kavi Rawat'}
                  </strong>
                  <span style={{ fontSize: '10px', fontWeight: 700, color: '#64748B', background: '#F1F5F9', border: '1px solid #CBD5E1', padding: '1px 6px', borderRadius: '4px' }}>
                    #{selectedOrder.id}
                  </span>
                </div>

                {/* Simulated Delivery Verification OTP Card */}
                <div style={{
                  background: '#FFFFFF',
                  borderRadius: '12px',
                  border: selectedOrder.isOtpVerified ? '1.5px solid #16A34A' : '1.5px solid #F59E0B',
                  padding: '14px',
                  textAlign: 'center',
                  marginBottom: '12px'
                }}>
                  <div style={{ fontSize: '11px', fontWeight: '800', color: selectedOrder.isOtpVerified ? '#16A34A' : '#B45309', letterSpacing: '1px', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '6px' }}>
                    {selectedOrder.isOtpVerified ? (
                      <>
                        <FaCheckCircle /> DELIVERY OTP VERIFIED
                      </>
                    ) : (
                      <>
                        <FaKey /> DELIVERY VERIFICATION OTP
                      </>
                    )}
                  </div>
                  <div style={{
                    fontSize: '26px',
                    fontWeight: '900',
                    letterSpacing: '6px',
                    color: selectedOrder.isOtpVerified ? '#16A34A' : '#78350F',
                    background: selectedOrder.isOtpVerified ? '#DCFCE7' : '#FEF3C7',
                    padding: '8px',
                    borderRadius: '8px',
                    margin: '8px 0'
                  }}>
                    {selectedOrder.deliveryOtp}
                  </div>
                  <div style={{ fontSize: '10px', color: '#64748B', lineHeight: '1.4' }}>
                    {selectedOrder.isOtpVerified
                      ? 'Order successfully delivered with this verified OTP.'
                      : 'Share this OTP with rider upon arrival.'}
                  </div>
                </div>

                {/* Simulated Admin Situation Card */}
                <div style={{
                  background: '#FFFFFF',
                  borderRadius: '12px',
                  border: '1px solid #E2E8F0',
                  padding: '12px',
                  marginBottom: '12px'
                }}>
                  <div style={{ fontSize: '11px', fontWeight: '800', color: '#475569', marginBottom: '6px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <FaBullhorn /> LIVE ORDER STATUS
                  </div>
                  <div style={{
                    background: selectedOrder.status === 'PREPARING' ? '#FFEDD5' : selectedOrder.status === 'OUT_FOR_DELIVERY' ? '#DBEAFE' : '#DCFCE7',
                    color: selectedOrder.status === 'PREPARING' ? '#C2410C' : selectedOrder.status === 'OUT_FOR_DELIVERY' ? '#1D4ED8' : '#15803D',
                    padding: '8px',
                    borderRadius: '6px',
                    fontSize: '12px',
                    fontWeight: '700',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '6px'
                  }}>
                    {selectedOrder.status === 'PREPARING' && (
                      <>
                        <FaUtensils /> Preparing in kitchen
                      </>
                    )}
                    {selectedOrder.status === 'OUT_FOR_DELIVERY' && (
                      <>
                        <FaMotorcycle /> Out for delivery
                      </>
                    )}
                    {selectedOrder.status === 'DELIVERED' && (
                      <>
                        <FaCheckCircle /> Delivered
                      </>
                    )}
                    {selectedOrder.status === 'PLACED' && (
                      <>
                        <FaClock /> Order Placed
                      </>
                    )}
                  </div>
                  {selectedOrder.adminNotes && (
                    <div style={{ fontSize: '11px', color: '#64748B', marginTop: '6px' }}>
                      {selectedOrder.adminNotes}
                    </div>
                  )}
                </div>

                {/* Items preview */}
                <div style={{
                  background: '#FFFFFF',
                  borderRadius: '10px',
                  padding: '10px',
                  border: '1px solid #E2E8F0',
                  fontSize: '11px'
                }}>
                  <strong>{selectedOrder.restaurantName}</strong>
                  <div style={{ color: '#64748B', marginTop: '4px' }}>
                    {selectedOrder.items?.map((it, i) => (
                      <div key={i}>{it.name} × {it.quantity}</div>
                    ))}
                  </div>
                  <div style={{ marginTop: '6px', fontWeight: '700', color: '#E23744' }}>
                    Total Paid: ₹{selectedOrder.totalAmount}
                  </div>
                </div>
              </div>
            ) : (
              <div style={{ padding: '20px', textAlign: 'center', color: '#94A3B8', margin: 'auto' }}>
                Select an order to view customer screen.
              </div>
            )}
          </aside>
        )}
      </main>
    </div>
  );
}

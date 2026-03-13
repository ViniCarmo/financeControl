import { Route, Routes, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./routes/ProtectedRoute";
import { DashboardLayout } from "./components/Layout/DashboardLayout";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { DashboardPage } from "./pages/DashboardPage";
import { TransactionsPage } from "./pages/TransactionsPage";
import { ProfilePage } from "./pages/ProfilePage";

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <DashboardLayout>
                <DashboardPage />
              </DashboardLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/transactions"
          element=
          {(
            <ProtectedRoute>
              <DashboardLayout>
                <TransactionsPage />
              </DashboardLayout>
            </ProtectedRoute>
          )}
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <DashboardLayout>
                <ProfilePage />
              </DashboardLayout>
            </ProtectedRoute>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;


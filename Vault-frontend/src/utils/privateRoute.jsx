import { Navigate } from "react-router-dom";
const PrivateRoute = ({ children, admin }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  if (!token) return <Navigate to="/login" />;
  if (admin && role !== "USER") return <Navigate to="/" />;
  return children;
};
export default PrivateRoute;

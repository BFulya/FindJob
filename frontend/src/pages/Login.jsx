import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Alert from 'react-bootstrap/Alert';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loginAttempts, setLoginAttempts] = useState(0);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (loginAttempts >= 3) {
      setError('Account is locked due to too many failed attempts. Please contact support.');
      return;
    }

    const result = await login(username, password);

    if (result.success) {
      if (result.role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/job-seeker/dashboard');
      }
    } else {
      setError(result.error);
      setLoginAttempts(loginAttempts + 1);
      
      if (loginAttempts + 1 >= 3) {
        setError('Account is locked due to too many failed attempts. Please contact support.');
      }
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
      <Card style={{ width: '400px' }}>
        <Card.Header>
          <h4 className="text-center mb-0">Login</h4>
        </Card.Header>
        <Card.Body>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Username</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </Form.Group>
            <Button variant="primary" type="submit" className="w-100">
              Login
            </Button>
          </Form>
          <div className="text-center mt-3">
            <small>
              Don't have an account? <a href="/register">Register here</a>
            </small>
          </div>
          {loginAttempts > 0 && (
            <div className="text-center mt-2">
              <small className="text-muted">
                Remaining attempts: {3 - loginAttempts}
              </small>
            </div>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};

export default Login;

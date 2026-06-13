import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Alert from 'react-bootstrap/Alert';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    role: 'JOB_SEEKER',
    birthDate: '',
    militaryStatus: 'COMPLETED',
    yearsOfExperience: 0,
    education: '',
    skills: '',
    workExperience: '',
    portfolioUrl: '',
    availableForWork: true,
    expectedSalary: 0,
    companyName: '',
    companyAddress: '',
    taxNumber: '',
  });

  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const result = await register(formData);

    if (result.success) {
      navigate('/login');
    } else {
      setError(typeof result.error === 'object' 
        ? Object.values(result.error).join(', ') 
        : result.error);
    }
  };

  return (
    <div className="d-flex justify-content-center">
      <Card style={{ width: '800px' }}>
        <Card.Header>
          <h4 className="text-center mb-0">Register</h4>
        </Card.Header>
        <Card.Body>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Row>
              <Col md={6}>
                <h5 className="mb-3">Account Information</h5>
                <Form.Group className="mb-3">
                  <Form.Label>Role</Form.Label>
                  <Form.Select
                    name="role"
                    value={formData.role}
                    onChange={handleChange}
                    required
                  >
                    <option value="JOB_SEEKER">Job Seeker</option>
                    <option value="ADMIN">Company/Admin</option>
                  </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>First Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Last Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Phone Number</Form.Label>
                  <Form.Control
                    type="text"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                {formData.role === 'JOB_SEEKER' ? (
                  <>
                    <h5 className="mb-3">Job Seeker Information</h5>
                    <Form.Group className="mb-3">
                      <Form.Label>Birth Date</Form.Label>
                      <Form.Control
                        type="date"
                        name="birthDate"
                        value={formData.birthDate}
                        onChange={handleChange}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Military Status</Form.Label>
                      <Form.Select
                        name="militaryStatus"
                        value={formData.militaryStatus}
                        onChange={handleChange}
                      >
                        <option value="COMPLETED">Completed</option>
                        <option value="EXEMPTED">Exempted</option>
                        <option value="POSTPONED">Postponed</option>
                        <option value="NOT_REQUIRED">Not Required</option>
                      </Form.Select>
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Years of Experience</Form.Label>
                      <Form.Control
                        type="number"
                        name="yearsOfExperience"
                        value={formData.yearsOfExperience}
                        onChange={handleChange}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Education</Form.Label>
                      <Form.Control
                        type="text"
                        name="education"
                        value={formData.education}
                        onChange={handleChange}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Skills</Form.Label>
                      <Form.Control
                        as="textarea"
                        name="skills"
                        value={formData.skills}
                        onChange={handleChange}
                        rows={2}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Work Experience</Form.Label>
                      <Form.Control
                        as="textarea"
                        name="workExperience"
                        value={formData.workExperience}
                        onChange={handleChange}
                        rows={2}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Portfolio URL</Form.Label>
                      <Form.Control
                        type="text"
                        name="portfolioUrl"
                        value={formData.portfolioUrl}
                        onChange={handleChange}
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Expected Salary</Form.Label>
                      <Form.Control
                        type="number"
                        name="expectedSalary"
                        value={formData.expectedSalary}
                        onChange={handleChange}
                      />
                    </Form.Group>
                  </>
                ) : (
                  <>
                    <h5 className="mb-3">Company Information</h5>
                    <Form.Group className="mb-3">
                      <Form.Label>Company Name</Form.Label>
                      <Form.Control
                        type="text"
                        name="companyName"
                        value={formData.companyName}
                        onChange={handleChange}
                        required
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Company Address</Form.Label>
                      <Form.Control
                        type="text"
                        name="companyAddress"
                        value={formData.companyAddress}
                        onChange={handleChange}
                        required
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Tax Number</Form.Label>
                      <Form.Control
                        type="text"
                        name="taxNumber"
                        value={formData.taxNumber}
                        onChange={handleChange}
                        required
                      />
                    </Form.Group>
                  </>
                )}
              </Col>
            </Row>
            <Button variant="primary" type="submit" className="w-100 mt-3">
              Register
            </Button>
          </Form>
          <div className="text-center mt-3">
            <small>
              Already have an account? <a href="/login">Login here</a>
            </small>
          </div>
        </Card.Body>
      </Card>
    </div>
  );
};

export default Register;

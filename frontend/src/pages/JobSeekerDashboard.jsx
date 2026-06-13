import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Alert from 'react-bootstrap/Alert';

const JobSeekerDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [profile, setProfile] = useState(null);
  const [filterCriteria, setFilterCriteria] = useState({
    location: '',
    department: '',
    minSalary: '',
    maxExperience: '',
    militaryStatus: '',
  });

  useEffect(() => {
    if (user?.role !== 'JOB_SEEKER') {
      navigate('/login');
      return;
    }
    fetchJobs();
    fetchProfile();
  }, [user, navigate]);

  const fetchJobs = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/jobs/filter', filterCriteria);
      setJobs(response.data.content || []);
    } catch (error) {
      console.error('Error fetching jobs:', error);
    }
  };

  const fetchProfile = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/job-seekers/profile', {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      setProfile(response.data);
    } catch (error) {
      console.error('Error fetching profile:', error);
    }
  };

  const handleFilterChange = (e) => {
    setFilterCriteria({
      ...filterCriteria,
      [e.target.name]: e.target.value,
    });
  };

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    fetchJobs();
  };

  const handleApply = async (jobId) => {
    if (window.confirm('Are you sure you want to apply for this job?')) {
      try {
        await axios.post(`http://localhost:8080/api/jobs/${jobId}/apply`, {}, {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
        });
        alert('Application submitted successfully!');
      } catch (error) {
        console.error('Error applying for job:', error);
        alert(error.response?.data || 'Error applying for job');
      }
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Job Seeker Dashboard</h2>
        <Button variant="danger" onClick={logout}>
          Logout
        </Button>
      </div>

      {profile && (
        <Card className="mb-4">
          <Card.Header>
            <h5 className="mb-0">My Profile</h5>
          </Card.Header>
          <Card.Body>
            <Row>
              <Col md={6}>
                <p><strong>Name:</strong> {profile.firstName} {profile.lastName}</p>
                <p><strong>Email:</strong> {profile.email}</p>
                <p><strong>Phone:</strong> {profile.phoneNumber}</p>
              </Col>
              <Col md={6}>
                <p><strong>Experience:</strong> {profile.yearsOfExperience} years</p>
                <p><strong>Military Status:</strong> {profile.militaryStatus}</p>
                <p><strong>Education:</strong> {profile.education}</p>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      )}

      <Card className="filter-section">
        <Card.Header>
          <h5 className="mb-0">Filter Jobs</h5>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={handleFilterSubmit}>
            <Row>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    name="location"
                    value={filterCriteria.location}
                    onChange={handleFilterChange}
                    placeholder="e.g., Istanbul"
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Department</Form.Label>
                  <Form.Control
                    type="text"
                    name="department"
                    value={filterCriteria.department}
                    onChange={handleFilterChange}
                    placeholder="e.g., IT"
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Min Salary</Form.Label>
                  <Form.Control
                    type="number"
                    name="minSalary"
                    value={filterCriteria.minSalary}
                    onChange={handleFilterChange}
                    placeholder="e.g., 5000"
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Max Experience Required</Form.Label>
                  <Form.Control
                    type="number"
                    name="maxExperience"
                    value={filterCriteria.maxExperience}
                    onChange={handleFilterChange}
                    placeholder="e.g., 5"
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Military Status</Form.Label>
                  <Form.Select
                    name="militaryStatus"
                    value={filterCriteria.militaryStatus}
                    onChange={handleFilterChange}
                  >
                    <option value="">All</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="EXEMPTED">Exempted</option>
                    <option value="POSTPONED">Postponed</option>
                    <option value="NOT_REQUIRED">Not Required</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>
            <Button variant="primary" type="submit">
              Search Jobs
            </Button>
          </Form>
        </Card.Body>
      </Card>

      <h3 className="mb-3">Available Jobs</h3>
      
      {jobs.length === 0 ? (
        <Alert variant="info">No jobs found matching your criteria.</Alert>
      ) : (
        <Row>
          {jobs.map((job) => (
            <Col md={4} key={job.id} className="mb-4">
              <Card className="job-card h-100">
                <Card.Header>
                  <h5 className="mb-0">{job.title}</h5>
                  <small className="text-white">{job.postedByCompanyName}</small>
                </Card.Header>
                <Card.Body>
                  <p><strong>Location:</strong> {job.location}</p>
                  <p><strong>Department:</strong> {job.department}</p>
                  <p><strong>Salary:</strong> ${job.salaryMin} - ${job.salaryMax}</p>
                  <p><strong>Required Experience:</strong> {job.requiredExperience} years</p>
                  <p><strong>Military Status:</strong> {job.requiredMilitaryStatus}</p>
                  <p><strong>Education:</strong> {job.requiredEducation}</p>
                  <p><strong>Deadline:</strong> {job.applicationDeadline}</p>
                  <p className="text-muted small">{job.description}</p>
                </Card.Body>
                <Card.Footer>
                  <Button
                    variant="primary"
                    className="w-100"
                    onClick={() => handleApply(job.id)}
                  >
                    Apply Now
                  </Button>
                </Card.Footer>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
};

export default JobSeekerDashboard;

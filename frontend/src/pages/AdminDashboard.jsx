import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Table from 'react-bootstrap/Table';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Modal from 'react-bootstrap/Modal';
import Alert from 'react-bootstrap/Alert';

const AdminDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('jobs');
  const [jobs, setJobs] = useState([]);
  const [jobSeekers, setJobSeekers] = useState([]);
  const [showJobModal, setShowJobModal] = useState(false);
  const [filterCriteria, setFilterCriteria] = useState({
    militaryStatus: '',
    minExperience: '',
    maxSalary: '',
    education: '',
  });
  const [newJob, setNewJob] = useState({
    title: '',
    description: '',
    location: '',
    department: '',
    salaryMin: '',
    salaryMax: '',
    requiredExperience: '',
    requiredMilitaryStatus: 'COMPLETED',
    requiredEducation: '',
    requiredSkills: '',
    applicationDeadline: '',
  });

  useEffect(() => {
    if (user?.role !== 'ADMIN') {
      navigate('/login');
      return;
    }
    fetchJobs();
  }, [user, navigate]);

  const fetchJobs = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/jobs/admin/' + user?.username, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      setJobs(response.data.content || []);
    } catch (error) {
      console.error('Error fetching jobs:', error);
    }
  };

  const fetchJobSeekers = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/job-seekers/filter', filterCriteria, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      setJobSeekers(response.data.content || []);
    } catch (error) {
      console.error('Error fetching job seekers:', error);
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
    fetchJobSeekers();
  };

  const handleJobSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/jobs', newJob, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      setShowJobModal(false);
      fetchJobs();
      setNewJob({
        title: '',
        description: '',
        location: '',
        department: '',
        salaryMin: '',
        salaryMax: '',
        requiredExperience: '',
        requiredMilitaryStatus: 'COMPLETED',
        requiredEducation: '',
        requiredSkills: '',
        applicationDeadline: '',
      });
    } catch (error) {
      console.error('Error creating job:', error);
    }
  };

  const handleDeleteJob = async (jobId) => {
    if (window.confirm('Are you sure you want to delete this job?')) {
      try {
        await axios.delete(`http://localhost:8080/api/jobs/${jobId}`, {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
        });
        fetchJobs();
      } catch (error) {
        console.error('Error deleting job:', error);
      }
    }
  };

  const handleSaveToFile = async (jobSeekerId) => {
    try {
      await axios.post(`http://localhost:8080/api/job-seekers/${jobSeekerId}/save-to-file`, {}, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      alert('Job seeker data saved to file successfully');
    } catch (error) {
      console.error('Error saving to file:', error);
      alert('Error saving to file');
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Admin Dashboard</h2>
        <Button variant="danger" onClick={logout}>
          Logout
        </Button>
      </div>

      <div className="mb-4">
        <Button
          variant={activeTab === 'jobs' ? 'primary' : 'outline-primary'}
          onClick={() => setActiveTab('jobs')}
          className="me-2"
        >
          My Jobs
        </Button>
        <Button
          variant={activeTab === 'seekers' ? 'primary' : 'outline-primary'}
          onClick={() => setActiveTab('seekers')}
        >
          Filter Job Seekers
        </Button>
      </div>

      {activeTab === 'jobs' && (
        <div>
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h3>My Job Postings</h3>
            <Button variant="primary" onClick={() => setShowJobModal(true)}>
              + Post New Job
            </Button>
          </div>

          {jobs.length === 0 ? (
            <Alert variant="info">No job postings yet. Create your first job posting!</Alert>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Location</th>
                  <th>Department</th>
                  <th>Salary Range</th>
                  <th>Required Experience</th>
                  <th>Deadline</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobs.map((job) => (
                  <tr key={job.id}>
                    <td>{job.title}</td>
                    <td>{job.location}</td>
                    <td>{job.department}</td>
                    <td>${job.salaryMin} - ${job.salaryMax}</td>
                    <td>{job.requiredExperience} years</td>
                    <td>{job.applicationDeadline}</td>
                    <td>
                      <span className={`badge ${job.isActive ? 'bg-success' : 'bg-danger'}`}>
                        {job.isActive ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleDeleteJob(job.id)}
                      >
                        Delete
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </div>
      )}

      {activeTab === 'seekers' && (
        <div>
          <h3 className="mb-3">Filter Job Seekers</h3>
          
          <Card className="filter-section">
            <Form onSubmit={handleFilterSubmit}>
              <Row>
                <Col md={3}>
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
                <Col md={3}>
                  <Form.Group className="mb-3">
                    <Form.Label>Min Experience (years)</Form.Label>
                    <Form.Control
                      type="number"
                      name="minExperience"
                      value={filterCriteria.minExperience}
                      onChange={handleFilterChange}
                    />
                  </Form.Group>
                </Col>
                <Col md={3}>
                  <Form.Group className="mb-3">
                    <Form.Label>Max Salary</Form.Label>
                    <Form.Control
                      type="number"
                      name="maxSalary"
                      value={filterCriteria.maxSalary}
                      onChange={handleFilterChange}
                    />
                  </Form.Group>
                </Col>
                <Col md={3}>
                  <Form.Group className="mb-3">
                    <Form.Label>Education</Form.Label>
                    <Form.Control
                      type="text"
                      name="education"
                      value={filterCriteria.education}
                      onChange={handleFilterChange}
                      placeholder="e.g., Bachelor's"
                    />
                  </Form.Group>
                </Col>
              </Row>
              <Button variant="primary" type="submit">
                Filter
              </Button>
            </Form>
          </Card>

          <h4 className="mb-3">Results</h4>
          
          {jobSeekers.length === 0 ? (
            <Alert variant="info">No job seekers found matching the criteria.</Alert>
          ) : (
            <Table striped bordered hover responsive>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Experience</th>
                  <th>Military Status</th>
                  <th>Education</th>
                  <th>Expected Salary</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobSeekers.map((seeker) => (
                  <tr key={seeker.id}>
                    <td>{seeker.firstName} {seeker.lastName}</td>
                    <td>{seeker.email}</td>
                    <td>{seeker.phoneNumber}</td>
                    <td>{seeker.yearsOfExperience} years</td>
                    <td>{seeker.militaryStatus}</td>
                    <td>{seeker.education}</td>
                    <td>${seeker.expectedSalary}</td>
                    <td>
                      <Button
                        variant="success"
                        size="sm"
                        onClick={() => handleSaveToFile(seeker.id)}
                      >
                        Save to File
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </div>
      )}

      <Modal show={showJobModal} onHide={() => setShowJobModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Post New Job</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleJobSubmit}>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Job Title</Form.Label>
                  <Form.Control
                    type="text"
                    name="title"
                    value={newJob.title}
                    onChange={(e) => setNewJob({ ...newJob, title: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    name="location"
                    value={newJob.location}
                    onChange={(e) => setNewJob({ ...newJob, location: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Department</Form.Label>
                  <Form.Control
                    type="text"
                    name="department"
                    value={newJob.department}
                    onChange={(e) => setNewJob({ ...newJob, department: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Required Experience (years)</Form.Label>
                  <Form.Control
                    type="number"
                    name="requiredExperience"
                    value={newJob.requiredExperience}
                    onChange={(e) => setNewJob({ ...newJob, requiredExperience: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Min Salary</Form.Label>
                  <Form.Control
                    type="number"
                    name="salaryMin"
                    value={newJob.salaryMin}
                    onChange={(e) => setNewJob({ ...newJob, salaryMin: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Max Salary</Form.Label>
                  <Form.Control
                    type="number"
                    name="salaryMax"
                    value={newJob.salaryMax}
                    onChange={(e) => setNewJob({ ...newJob, salaryMax: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                name="description"
                value={newJob.description}
                onChange={(e) => setNewJob({ ...newJob, description: e.target.value })}
                rows={3}
                required
              />
            </Form.Group>
            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Required Military Status</Form.Label>
                  <Form.Select
                    name="requiredMilitaryStatus"
                    value={newJob.requiredMilitaryStatus}
                    onChange={(e) => setNewJob({ ...newJob, requiredMilitaryStatus: e.target.value })}
                  >
                    <option value="COMPLETED">Completed</option>
                    <option value="EXEMPTED">Exempted</option>
                    <option value="POSTPONED">Postponed</option>
                    <option value="NOT_REQUIRED">Not Required</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Required Education</Form.Label>
                  <Form.Control
                    type="text"
                    name="requiredEducation"
                    value={newJob.requiredEducation}
                    onChange={(e) => setNewJob({ ...newJob, requiredEducation: e.target.value })}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>
            <Form.Group className="mb-3">
              <Form.Label>Required Skills</Form.Label>
              <Form.Control
                as="textarea"
                name="requiredSkills"
                value={newJob.requiredSkills}
                onChange={(e) => setNewJob({ ...newJob, requiredSkills: e.target.value })}
                rows={2}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Application Deadline</Form.Label>
              <Form.Control
                type="date"
                name="applicationDeadline"
                value={newJob.applicationDeadline}
                onChange={(e) => setNewJob({ ...newJob, applicationDeadline: e.target.value })}
                required
              />
            </Form.Group>
            <Button variant="primary" type="submit" className="w-100">
              Post Job
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default AdminDashboard;

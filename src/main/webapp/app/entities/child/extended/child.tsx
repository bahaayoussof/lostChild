import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Card, Col, Container, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, reset } from './child.reducer';
import { IChild } from 'app/shared/model/child.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import './child.scss';

export const Child = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const childList = useAppSelector(state => state.child.entities);
  const loading = useAppSelector(state => state.child.loading);
  const totalItems = useAppSelector(state => state.child.totalItems);
  const links = useAppSelector(state => state.child.links);
  const entity = useAppSelector(state => state.child.entity);
  const updateSuccess = useAppSelector(state => state.child.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="child-heading" data-cy="ChildHeading">
        {/* <Translate contentKey="lostChildApp.child.home.title">Children</Translate> */}
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lostChildApp.child.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lostChildApp.child.home.createLabel">Create new Child</Translate>
          </Link>
        </div>
      </h2>
      <div>
        <InfiniteScroll
          dataLength={childList ? childList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {childList && childList.length > 0 ? (
            <Container fluid>
              <Row>
                {childList.map((child, i) => (
                  <Col md={6} lg={4} key={`entity-${i}`}>
                    <Card className="grid-card">
                      <Row>
                        <Col xs={3}>
                          <div className="grid-card-image">
                            {child.image ? (
                              <div>
                                {child.imageContentType ? <img src={`data:${child.imageContentType};base64,${child.image}`} /> : null}
                              </div>
                            ) : null}
                          </div>
                        </Col>
                        <Col xs={9}>
                          <div className="grid-card-header">
                            <p className="child-name">
                              <h6>{child.name}</h6>
                            </p>
                            <p>
                              <label>Birthdate:</label>
                              {child.birthdate ? <TextFormat type="date" value={child.birthdate} format={APP_DATE_FORMAT} /> : null}
                            </p>
                            <p>
                              <label>Age:</label>
                              {child.age} Years
                            </p>
                          </div>
                        </Col>
                      </Row>
                      <hr />
                      <Row>
                        <Col xs={7}>
                          <div className="address-details">
                            <p>
                              <label>Country:</label>
                              {child.address ? child.address.country : ''}
                            </p>
                            <p>
                              <label>State:</label>
                              {child.address ? child.address.state : ''}
                            </p>
                            <p>
                              <label>City:</label>
                              {child.address ? child.address.city : ''}
                            </p>
                          </div>
                        </Col>
                        <Col xs={5}>
                          <div>
                            <p>
                              <label>Gender:</label>
                              <Translate contentKey={`lostChildApp.Gender.${child.gender}`} />
                            </p>
                            <p>
                              <label>Status:</label>
                              <Translate contentKey={`lostChildApp.Status.${child.status}`} />
                            </p>
                            <p>
                              <label>Agency:</label>
                              {child.agency}
                            </p>
                          </div>
                        </Col>
                      </Row>
                      <div className="grid-card-btns flex-btn-group-container">
                        <Button
                          className="grid-card-btn"
                          tag={Link}
                          to={`${match.url}/${child.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        {isAdmin && (
                          <Button
                            className="grid-card-btn"
                            tag={Link}
                            to={`${match.url}/${child.id}/delete`}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.delete">Delete</Translate>
                            </span>
                          </Button>
                        )}
                      </div>
                    </Card>
                  </Col>
                ))}
              </Row>
            </Container>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="lostChildApp.child.home.notFound">No Children found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Child;

import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './description.reducer';
import { IDescription } from 'app/shared/model/description.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Description = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const descriptionList = useAppSelector(state => state.description.entities);
  const loading = useAppSelector(state => state.description.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="description-heading" data-cy="DescriptionHeading">
        <Translate contentKey="lostChildApp.description.home.title">Descriptions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lostChildApp.description.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lostChildApp.description.home.createLabel">Create new Description</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {descriptionList && descriptionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="lostChildApp.description.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.description.eyeColor">Eye Color</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.description.hairColor">Hair Color</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.description.weight">Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.description.height">Height</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {descriptionList.map((description, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${description.id}`} color="link" size="sm">
                      {description.id}
                    </Button>
                  </td>
                  <td>{description.eyeColor}</td>
                  <td>{description.hairColor}</td>
                  <td>{description.weight}</td>
                  <td>{description.height}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${description.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${description.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${description.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="lostChildApp.description.home.notFound">No Descriptions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Description;

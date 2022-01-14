import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAddress } from 'app/shared/model/address.model';
import { getEntities as getAddresses } from 'app/entities/address/address.reducer';
import { IDescription } from 'app/shared/model/description.model';
import { getEntities as getDescriptions } from 'app/entities/description/description.reducer';
import { IContactInformation } from 'app/shared/model/contact-information.model';
import { getEntities as getContactInformations } from 'app/entities/contact-information/contact-information.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './child.reducer';
import { IChild } from 'app/shared/model/child.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export const ChildUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const addresses = useAppSelector(state => state.address.entities);
  const descriptions = useAppSelector(state => state.description.entities);
  const contactInformations = useAppSelector(state => state.contactInformation.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const childEntity = useAppSelector(state => state.child.entity);
  const loading = useAppSelector(state => state.child.loading);
  const updating = useAppSelector(state => state.child.updating);
  const updateSuccess = useAppSelector(state => state.child.updateSuccess);
  const genderValues = Object.keys(Gender);
  const statusValues = Object.keys(Status);
  const handleClose = () => {
    props.history.push('/child');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAddresses({}));
    dispatch(getDescriptions({}));
    dispatch(getContactInformations({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...childEntity,
      ...values,
      address: addresses.find(it => it.id.toString() === values.address.toString()),
      description: descriptions.find(it => it.id.toString() === values.description.toString()),
      contactInformation: contactInformations.find(it => it.id.toString() === values.contactInformation.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          gender: 'Male',
          status: 'Open',
          ...childEntity,
          address: childEntity?.address?.id,
          description: childEntity?.description?.id,
          contactInformation: childEntity?.contactInformation?.id,
          user: childEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lostChildApp.child.home.createOrEditLabel" data-cy="ChildCreateUpdateHeading">
            <Translate contentKey="lostChildApp.child.home.createOrEditLabel">Create or edit a Child</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="child-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('lostChildApp.child.image')}
                id="child-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('lostChildApp.child.name')}
                id="child-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('lostChildApp.child.age')}
                id="child-age"
                name="age"
                data-cy="age"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 18, message: translate('entity.validation.max', { max: 18 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('lostChildApp.child.gender')} id="child-gender" name="gender" data-cy="gender" type="select">
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {translate('lostChildApp.Gender.' + gender)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('lostChildApp.child.birthdate')}
                id="child-birthdate"
                name="birthdate"
                data-cy="birthdate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('lostChildApp.child.status')} id="child-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('lostChildApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('lostChildApp.child.agency')}
                id="child-agency"
                name="agency"
                data-cy="agency"
                type="text"
                validate={{
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 5, message: translate('entity.validation.maxlength', { max: 5 }) },
                }}
              />
              <ValidatedField
                id="child-address"
                name="address"
                data-cy="address"
                label={translate('lostChildApp.child.address')}
                type="select"
              >
                <option value="" key="0" />
                {addresses
                  ? addresses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="child-description"
                name="description"
                data-cy="description"
                label={translate('lostChildApp.child.description')}
                type="select"
              >
                <option value="" key="0" />
                {descriptions
                  ? descriptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="child-contactInformation"
                name="contactInformation"
                data-cy="contactInformation"
                label={translate('lostChildApp.child.contactInformation')}
                type="select"
              >
                <option value="" key="0" />
                {contactInformations
                  ? contactInformations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-user" name="user" data-cy="user" label={translate('lostChildApp.child.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/child" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ChildUpdate;

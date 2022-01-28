import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Form } from 'reactstrap';
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
import { useForm } from 'react-hook-form';
import ChildLastSeen from './child-last-seen';
/* eslint-disable no-console */

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
  const [updateImage] = useState(!isNew ? 'data:' + childEntity.imageContentType + ';base64,' + childEntity.image : null);

  const [img, setImg] = useState(null);
  const [image, setImage] = useState(!isNew && updateImage ? updateImage.split(',')[1] : null);

  const getBase64 = file => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
      setImage(reader.result.toString().split(',')[1]);
    };
    reader.onerror = function (error) {
      console.log('Error: ', error);
    };
  };

  const onImageChange = event => {
    if (event.target.files && event.target.files[0]) {
      setImg(URL.createObjectURL(event.target.files[0]));
      getBase64(event.target.files[0]);
    }
  };

  let lastSeens = [];
  const handleLastSeens = lseen => {
    lastSeens = [...lastSeens, lseen];
  };

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
    const imageContentType = 'image/.*';
    console.log('🚀 ~ file: child-update.tsx ~ line 94 ~ ChildUpdate ~ image', image);
    const entity = {
      ...childEntity,
      ...values,
      image,
      imageContentType,
      lastSeens,
      // address: addresses.find(it => it.id.toString() === values.address.toString()),
      // description: descriptions.find(it => it.id.toString() === values.description.toString()),
      // contactInformation: contactInformations.find(it => it.id.toString() === values.contactInformation.toString()),
      // user: users.find(it => it.id.toString() === values.user.toString()),
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
          img: childEntity?.image,
          // address: childEntity?.address,
          // description: childEntity?.description,
          // contactInformation: childEntity?.contactInformation,
        };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ defaultValues: defaultValues(), mode: 'onTouched', shouldUseNativeValidation: true });

  return (
    <div className="add-edit-child">
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
            <Form onSubmit={handleSubmit(saveEntity)}>
              <Row className="child-info">
                <h5 className="header">Child Information</h5>
                <Col md={6}>
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
                    register={register}
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
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.child.gender')}
                    id="child-gender"
                    name="gender"
                    data-cy="gender"
                    type="select"
                    register={register}
                  >
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
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.child.status')}
                    id="child-status"
                    name="status"
                    data-cy="status"
                    type="select"
                    register={register}
                  >
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
                    register={register}
                  />
                </Col>
                <Col md={6}>
                  {!isNew ? (
                    <ValidatedField
                      name="id"
                      required
                      readOnly
                      id="child-id"
                      label={translate('global.field.id')}
                      validate={{ required: true }}
                      register={register}
                    />
                  ) : null}
                  <div>
                    <label>Image</label>
                    <input
                      type="file"
                      onChange={e => onImageChange(e)}
                      className="image-upload form-control"
                      id="child-image"
                      name="image"
                      data-cy="image"
                      accept="image/.*"
                    />
                    {img ? <img className="image-preview" src={img} alt={childEntity.name} /> : null}
                    {!img && updateImage ? <img className="image-preview" src={updateImage} alt={childEntity.name} /> : null}
                  </div>
                </Col>
              </Row>
              <Row className="child-contact-info">
                <h5 className="header">Contact Information</h5>
                <Col md={6}>
                  <ValidatedField
                    label={translate('lostChildApp.contactInformation.name')}
                    id="contact-information-name"
                    name="contactInformation.name"
                    data-cy="name"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                      maxLength: { value: 20, message: "translate('entity.validation.maxlength', { max: 20 })" },
                    }}
                    register={register}
                  />
                </Col>
                <Col md={6}>
                  <ValidatedField
                    label={translate('lostChildApp.contactInformation.phoneNumber')}
                    id="contact-information-phoneNumber"
                    name="contactInformation.phoneNumber"
                    data-cy="phoneNumber"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                      maxLength: { value: 11, message: translate('entity.validation.maxlength', { max: 11 }) },
                      pattern: { value: /^[0-9]*$/, message: 'this field should be numbers' },
                    }}
                    register={register}
                  />
                </Col>
                <ValidatedField
                  label={translate('lostChildApp.contactInformation.email')}
                  placeholder="name@doman.com"
                  id="contact-information-email"
                  name="contactInformation.email"
                  data-cy="email"
                  type="text"
                  validate={{
                    required: { value: true, message: translate('entity.validation.required') },
                    pattern: {
                      value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                      message: translate('entity.validation.pattern', { pattern: 'name@doman.com' }),
                    },
                  }}
                  register={register}
                />
              </Row>
              <Row className="child-address-description">
                <Col md={6}>
                  <h5 className="header">Address</h5>
                  <ValidatedField
                    label={translate('lostChildApp.address.street')}
                    id="address-street"
                    name="address.street"
                    data-cy="street"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.address.city')}
                    id="address-city"
                    name="address.city"
                    data-cy="city"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.address.state')}
                    id="address-state"
                    name="address.state"
                    data-cy="state"
                    type="text"
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.address.country')}
                    id="address-country"
                    name="address.country"
                    data-cy="country"
                    type="text"
                    register={register}
                  />
                </Col>
                <Col md={6}>
                  <h5 className="header">Description</h5>
                  <ValidatedField
                    label={translate('lostChildApp.description.eyeColor')}
                    id="description-eyeColor"
                    name="description.eyeColor"
                    data-cy="eyeColor"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.description.hairColor')}
                    id="description-hairColor"
                    name="description.hairColor"
                    data-cy="hairColor"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.description.weight')}
                    id="description-weight"
                    name="description.weight"
                    data-cy="weight"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                    register={register}
                  />
                  <ValidatedField
                    label={translate('lostChildApp.description.height')}
                    id="description-height"
                    name="description.height"
                    data-cy="height"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                    register={register}
                  />
                </Col>
              </Row>
              <Row className="child-last-seen">
                <ChildLastSeen handleLastSeens={handleLastSeens} childLastSeens={childEntity.lastSeens} />
              </Row>
              <Row>
                <div className="child-details-buttons">
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
                </div>
              </Row>
            </Form>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ChildUpdate;
